package ru.rosatom.documentflow.services.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.PPr;
import ru.rosatom.documentflow.dto.UserReplyDto;
import ru.rosatom.documentflow.exceptions.BadRequestException;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.DocAttributeValues;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.Document;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class FileServiceAbstract {

    public void addPreliminaryData(UserReplyDto userReplyDto, MainDocumentPart mainDocumentPart, Document document) {

        PPr paragraphProperties = new PPr();
        Jc justification = new Jc();

        justification.setVal(JcEnumeration.RIGHT);
        paragraphProperties.setJc(justification);
        mainDocumentPart.addStyledParagraphOfText("Title", document.getDocType().getName());
        mainDocumentPart.addParagraphOfText("ФИО: " + userReplyDto.getFullName()).setPPr(paragraphProperties);
        mainDocumentPart.addParagraphOfText("Организация: " + userReplyDto.getUserOrganization().getName()).setPPr(paragraphProperties);
        mainDocumentPart.addParagraphOfText("ИНН: " + userReplyDto.getUserOrganization().getInn()).setPPr(paragraphProperties);
        mainDocumentPart.addParagraphOfText("\n\n");
        mainDocumentPart.addParagraphOfText("Значения атрибутов:");

        for (DocAttributeValues attributeValue : document.getAttributeValues()) {
            mainDocumentPart.addParagraphOfText(attributeValue.getAttribute().getName() + ": " + attributeValue.getValue());
        }
    }

    public void addFinalData(MainDocumentPart mainDocumentPart, Collection<DocProcess> docProcess, UserMapper userMapper) {

        mainDocumentPart.addParagraphOfText("\n\n");
        mainDocumentPart.addParagraphOfText("Согласовали:");

        for (DocProcess process : docProcess) {
            if (process.getStatus().equals(DocProcessStatus.APPROVED)) {
                UserReplyDto recipientReplyDto = userMapper.objectToReplyDto(process.getRecipient());
                mainDocumentPart.addParagraphOfText("ФИО: " + recipientReplyDto.getFullName());
                mainDocumentPart.addParagraphOfText("Организация: " + recipientReplyDto.getUserOrganization().getName());
                mainDocumentPart.addParagraphOfText("ИНН: " + recipientReplyDto.getUserOrganization().getInn());
                mainDocumentPart.addParagraphOfText("Подпись:");
                mainDocumentPart.addParagraphOfText("\n\n");
            }
        }
    }

    public void convertToPdf(File fileDocx, File filePdf) {

        IConverter converter = LocalConverter.builder()
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(30, TimeUnit.SECONDS)
                .build();

        converter.convert(fileDocx).as(DocumentType.MS_WORD)
                .to(filePdf).as(DocumentType.PDF)
                .prioritizeWith(1000)
                .schedule();
    }

    public void deleteLocalFile(File file) {

        if (!file.delete()) {
            throw new BadRequestException("Ошибка при удалении файла");
        }
    }
}
