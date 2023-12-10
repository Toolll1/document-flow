package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.MessagePattern;
import ru.rosatom.documentflow.models.MessageTemplate;
import ru.rosatom.documentflow.services.EmailService;
import ru.rosatom.documentflow.services.MessageTemplateService;

import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractEmailService implements EmailService {

    protected final MessageTemplateService messageTemplateService;

    @Value("${spring.mail.username}")
    protected String from;

    @Override
    public void sendDocProcessAgreementMessageForRecipient(DocProcess docProcess) {
        MessageTemplate messageTemplate = messageTemplateService.getMessageTemplateByPattern(MessagePattern.AGREEMENT);
        Map<String, String> messageParams = Map.of(
                "firstName", docProcess.getRecipient().getFirstName(),
                "documentName", docProcess.getDocument().getName(),
                "mail", docProcess.getSender().getEmail(),
                "fullName", docProcess.getSender().getLastName() + ' ' + docProcess.getSender().getFirstName() + ' ' + docProcess.getSender().getPatronymic());
        StringSubstitutor substitutor = new StringSubstitutor(messageParams);
        sendDocProcessMessage(docProcess.getRecipient().getEmail(), substitutor.replace(messageTemplate.getBody()), messageTemplate.getSubject());
    }

    @Override
    public void sendDocProcessResultMessageForOwner(DocProcess docProcess, MessagePattern messagePattern) {
        MessageTemplate messageTemplate = messageTemplateService.getMessageTemplateByPattern(messagePattern);
        Map<String, String> messageParams = Map.of(
                "firstName", docProcess.getSender().getFirstName(),
                "documentName", docProcess.getDocument().getName());
        StringSubstitutor substitutor = new StringSubstitutor(messageParams);
        sendDocProcessMessage(docProcess.getSender().getEmail(), substitutor.replace(messageTemplate.getBody()), messageTemplate.getSubject());
    }

    @Override
    public void sendMessageWithNewComment(DocProcess docProcess){
        MessageTemplate messageTemplate = messageTemplateService.getMessageTemplateByPattern(MessagePattern.NEW_COMMENT);
        Map<String, String> messageParams = Map.of(
                "firstName", docProcess.getSender().getFirstName(),
                "documentName", docProcess.getDocument().getName(),
                "mail", docProcess.getSender().getEmail(),
                "fullName", docProcess.getSender().getFullName());
        StringSubstitutor substitutor = new StringSubstitutor(messageParams);
        sendDocProcessMessage(docProcess.getRecipient().getEmail(), substitutor.replace(messageTemplate.getBody()), messageTemplate.getSubject());
    }
}
