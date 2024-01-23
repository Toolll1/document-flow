package ru.rosatom.documentflow.services.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.adapters.TranslitText;
import ru.rosatom.documentflow.configuration.MinioConfig;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.exceptions.FileDownloadException;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.FileService;
import ru.rosatom.documentflow.services.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@Service
@ConditionalOnProperty(prefix = "service", name = "file", havingValue = "minio")
public class FileServiceMinioImpl extends FileServiceAbstract implements FileService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final WordprocessingMLPackage wordPackage;

    public FileServiceMinioImpl(MinioConfig minioConfig, UserService userService, UserMapper userMapper) {
        this.minioConfig = minioConfig;
        this.userService = userService;
        this.userMapper = userMapper;
        minioClient = MinioClient.builder()
                .endpoint(minioConfig.getEndpoint())
                // .endpoint(minioConfig.getLocalEndpoint()) // для запуска нашего сервиса локально
                .credentials(minioConfig.getLogin(), minioConfig.getPassword())
                .build();

        try {
            wordPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFile(Document document) {
        String bucketName = document.getDocumentPath().replace("http://127.0.0.1:9090/browser/", "");//todo поменять хттп
        String objectName = document.getName();

        try {
            GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(120)
                    .build();
            return minioClient.getPresignedObjectUrl(urlArgs);
        } catch (Exception e) {
            throw new FileDownloadException("Ошибка при скачивании файла: " + e.getMessage());
        }
    }


    @Override
    public Document createFile(Document document, Collection<DocProcess> docProcess) {

        User user = userService.getUser(document.getOwnerId());
        String nameDocx = TranslitText.transliterate(user.getLastName()).replaceAll(" ", "").toLowerCase() + System.currentTimeMillis() + ".docx";
        String pathDocx = FileSystems.getDefault().getPath("files", nameDocx).toAbsolutePath().toString();
        File fileDocx = new File(pathDocx);
        fileDocx.getParentFile().mkdirs();

        if (fileDocx.exists()) {
            throw new ConflictException("Такой файл уже существует");
        }

        try {
            UserReplyDto userReplyDto = userMapper.objectToReplyDto(user);
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();

            addPreliminaryData(userReplyDto, mainDocumentPart, document);

            if (docProcess != null) {
                addFinalData(mainDocumentPart, docProcess, userMapper);

                String namePdf = TranslitText.transliterate(user.getLastName()).replaceAll(" ", "").toLowerCase() + System.currentTimeMillis() + ".pdf";
                String pathPdf = FileSystems.getDefault().getPath("files", nameDocx).toAbsolutePath().toString();
                File filePdf = new File(pathPdf);

                wordPackage.save(fileDocx);
                convertToPdf(fileDocx, filePdf);
                deleteLocalFile(fileDocx);

                return addFileToMinio(document, namePdf, filePdf);
            } else {
                wordPackage.save(fileDocx);
            }

        } catch (Docx4JException e) {
            throw new BadRequestException(e.getMessage());
        }

        return addFileToMinio(document, nameDocx, fileDocx);
    }

    private Document addFileToMinio(Document document, String name, File file) {

        String bucketName = TranslitText.transliterate(document.getDocType().getName().replaceAll(" ", "").toLowerCase());

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .filename(file.getAbsolutePath())
                    .build());

        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new BadRequestException(e.getMessage());
        } finally {
            deleteLocalFile(file);
        }

        document.setDocumentPath("http://127.0.0.1:9000" + bucketName);
        document.setDocumentPath(minioConfig.getPrefix() + bucketName);
        document.setName(name);

        return document;
    }

    @Override
    public Document updateFile(Document newDocument, Document oldDocument, Collection<DocProcess> docProcess) {

        return editFileInMinio(newDocument, oldDocument, "modified-files", docProcess);
    }


    public Document editFileInMinio(Document newDocument, Document oldDocument, String basketVersionControl, Collection<DocProcess> docProcess) {

        String fileName = oldDocument.getName();
        String bucketName = oldDocument.getDocumentPath().replace("http://127.0.0.1:9000", "");

        try {
            copyFileFromMinio(fileName, bucketName, basketVersionControl);
            deleteFileInMinio(fileName, bucketName);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }

        if (basketVersionControl.equals("modified-files")) {
            return createFile(newDocument, docProcess);
        } else {
            return null;
        }
    }

    @Override
    public void deleteFile(Document oldDocument) {

        editFileInMinio(null, oldDocument, "deleted-files", null);
    }

    private void deleteFileInMinio(String fileName, String bucketName) throws RuntimeException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

    private void copyFileFromMinio(String fileName, String bucketName, String basketForControl) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(basketForControl).build());

        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(basketForControl).build());
        }

        minioClient.copyObject(
                CopyObjectArgs.builder()
                        .bucket(basketForControl)
                        .object(fileName)
                        .source(
                                CopySource.builder()
                                        .bucket(bucketName)
                                        .object(fileName)
                                        .build())
                        .build());
    }
}
