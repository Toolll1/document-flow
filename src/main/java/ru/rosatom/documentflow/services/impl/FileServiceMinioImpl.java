package ru.rosatom.documentflow.services.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.PPr;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.adapters.TranslitText;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.services.FileService;
import ru.rosatom.documentflow.services.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@ConditionalOnProperty(
        value = "project.mq.enabled",
        matchIfMissing = false)
@Service
@RequiredArgsConstructor
public class FileServiceMinioImpl implements FileService {

    private final MinioClient minioClient = MinioClient.builder()
            .endpoint("http://minio:9000")
            //.endpoint("http://localhost:9000") // для запуска нашего сервиса локально
            .credentials("admin", "Secure123$")
            .build();
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public Document createFile(Document document, Collection<DocProcess> docProcess) {

        User user = userService.getUser(document.getOwnerId());
        String nameDocx = TranslitText.transliterate(user.getLastName()).replaceAll(" ", "").toLowerCase() + System.currentTimeMillis() + ".docx";
        String pathDocx = FileSystems.getDefault().getPath("files", nameDocx).toAbsolutePath().toString();
        File fileDocx = new File(pathDocx);

        if (fileDocx.exists()) {
            throw new ConflictException("Такой файл уже существует");
        }

        try {
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            PPr paragraphProperties = new PPr();
            Jc justification = new Jc();
            UserReplyDto userReplyDto = userMapper.objectToReplyDto(user);

            justification.setVal(JcEnumeration.RIGHT);
            paragraphProperties.setJc(justification);
            mainDocumentPart.addStyledParagraphOfText("Title", document.getDocType().getName());
            mainDocumentPart.addParagraphOfText("ФИО: " + userReplyDto.getFullName()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("Организация: " + userReplyDto.getOrganization().getName()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("ИНН: " + userReplyDto.getOrganization().getInn()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("\n\n");
            mainDocumentPart.addParagraphOfText("Значения атрибутов:");

            for (DocAttributeValues attributeValue : document.getAttributeValues()) {
                mainDocumentPart.addParagraphOfText(attributeValue.getAttribute().getName() + ": " + attributeValue.getValue());
            }

            if (docProcess != null) {
                mainDocumentPart.addParagraphOfText("\n\n");
                mainDocumentPart.addParagraphOfText("Согласовали:");

                for (DocProcess process : docProcess) {
                    if (process.getStatus().equals(DocProcessStatus.APPROVED)) {
                        UserReplyDto recipientReplyDto = userMapper.objectToReplyDto(process.getRecipient());
                        mainDocumentPart.addParagraphOfText("ФИО: " + recipientReplyDto.getFullName());
                        mainDocumentPart.addParagraphOfText("Организация: " + recipientReplyDto.getOrganization().getName());
                        mainDocumentPart.addParagraphOfText("ИНН: " + recipientReplyDto.getOrganization().getInn());
                        mainDocumentPart.addParagraphOfText("Подпись:");
                        mainDocumentPart.addParagraphOfText("\n\n");
                    }
                }

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

    private void convertToPdf(File fileDocx, File filePdf) {

        IConverter converter = LocalConverter.builder()
                .baseFolder(new File("C:\\temp"))
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(30, TimeUnit.SECONDS)
                .build();

        converter.convert(fileDocx).as(DocumentType.MS_WORD)
                .to(filePdf).as(DocumentType.PDF)
                .prioritizeWith(1000)
                .schedule();
    }

    private void deleteLocalFile(File file) {

        if (!file.delete()) {
            throw new BadRequestException("Ошибка при удалении файла");
        }
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

        document.setDocumentPath("http://127.0.0.1:9090/browser/" + bucketName);
        document.setName(name);

        return document;
    }

    @Override
    public Document updateFile(Document newDocument, Document oldDocument, Collection<DocProcess> docProcess) {

        if (docProcess != null) {
            return editFileInMinio(newDocument, oldDocument, "modified-files", docProcess);
        }

        return editFileInMinio(newDocument, oldDocument, "modified-files", null);
    }


    public Document editFileInMinio(Document newDocument, Document oldDocument, String basketVersionControl, Collection<DocProcess> docProcess) {

        String fileName = oldDocument.getName();
        String bucketName = oldDocument.getDocumentPath().replace("http://127.0.0.1:9090/browser/", "");

        try {
            copyFileFromMinio(fileName, bucketName, basketVersionControl);
            deleteFileInMinio(fileName, bucketName);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }

        if (basketVersionControl.equals("modified-files") && docProcess != null)
            return createFile(newDocument, docProcess);
        else if (basketVersionControl.equals("modified-files")) return createFile(newDocument, null);
        else return null;
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
