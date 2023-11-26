package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.MessagePattern;
import ru.rosatom.documentflow.models.MessageTemplate;
import ru.rosatom.documentflow.services.EmailService;
import ru.rosatom.documentflow.services.MessageTemplateService;

import java.util.Map;

@Service
@Slf4j
@Profile("dev")
@RequiredArgsConstructor
public class EmailServiceStub implements EmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final MessageTemplateService messageTemplateService;


    @Override
    public void sendDocProcessMessage(String to, String text, String subject) {
        log.info("Message sent successfully\n" +
                "From: {}\n" +
                "To: {}\n" +
                "Subject: {}\n" +
                "Body: {}\n", from, to, subject, text);
    }

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


}