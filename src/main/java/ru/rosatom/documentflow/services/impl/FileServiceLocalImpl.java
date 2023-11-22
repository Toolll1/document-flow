package ru.rosatom.documentflow.services.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
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
import java.nio.file.FileSystems;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "service", name = "file", havingValue = "local")
public class FileServiceLocalImpl implements FileService {

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
            mainDocumentPart.addParagraphOfText("Дата рождения: " + userReplyDto.getDateOfBirth()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("Организация: " + userReplyDto.getOrgDto().getName()).setPPr(paragraphProperties);
            mainDocumentPart.addParagraphOfText("ИНН: " + userReplyDto.getOrgDto().getInn()).setPPr(paragraphProperties);
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
                        mainDocumentPart.addParagraphOfText("Организация: " + recipientReplyDto.getOrgDto().getName());
                        mainDocumentPart.addParagraphOfText("ИНН: " + recipientReplyDto.getOrgDto().getInn());
                        mainDocumentPart.addParagraphOfText("Подпись:");
                        mainDocumentPart.addParagraphOfText("\n\n");
                    }
                }

                String namePdf = TranslitText.transliterate(user.getLastName()).replaceAll(" ", "").toLowerCase() + System.currentTimeMillis() + ".pdf";
                String pathPdf = FileSystems.getDefault().getPath("files", nameDocx).toAbsolutePath().toString();
                File filePdf = new File(pathPdf);

                wordPackage.save(fileDocx);
               // convertToPdf(fileDocx, filePdf);
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

    @Override
    public Document updateFile(Document newDocument, Document oldDocument, Collection<DocProcess> docProcess) {

        deleteFile(oldDocument);

        if (docProcess != null) {
            return createFile(newDocument, docProcess);
        }

        return createFile(newDocument, null);
    }

    @Override
    public void deleteFile(Document document) {

        File file = new File(document.getDocumentPath());

        if (!file.delete()) {
            throw new BadRequestException("Ошибка при удалении файла");
        }
    }
}