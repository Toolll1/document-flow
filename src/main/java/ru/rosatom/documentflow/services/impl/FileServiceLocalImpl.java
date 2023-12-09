package ru.rosatom.documentflow.services.impl;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.adapters.TranslitText;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.exceptions.ConflictException;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.Document;
import ru.rosatom.documentflow.models.User;
import ru.rosatom.documentflow.services.FileService;
import ru.rosatom.documentflow.services.UserService;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Collection;

@Service
@ConditionalOnProperty(prefix = "service", name = "file", havingValue = "local")
public class FileServiceLocalImpl extends FileServiceAbstract implements FileService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final WordprocessingMLPackage wordPackage;

    public FileServiceLocalImpl(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;

        try {
            wordPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

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
                deleteFile(Document.builder().documentPath(pathDocx).build());
                document.setName(namePdf);
                document.setDocumentPath(pathPdf);
            } else {

                wordPackage.save(fileDocx);
                document.setName(nameDocx);
                document.setDocumentPath(pathDocx);
            }

        } catch (Docx4JException e) {
            throw new BadRequestException(e.getMessage());
        }

        return document;
    }

    @Override
    public Document updateFile(Document newDocument, Document oldDocument, Collection<DocProcess> docProcess) {

        deleteFile(oldDocument);

        return createFile(newDocument, docProcess);
    }

    @Override
    public void deleteFile(Document document) {

        File file = new File(document.getDocumentPath());

        deleteLocalFile(file);
    }
}