package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.MessagePattern;
import ru.rosatom.documentflow.models.MessageTemplate;
import ru.rosatom.documentflow.services.EmailService;
import ru.rosatom.documentflow.services.MessageTemplateService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String from;

    private final String MESSAGE_ENCODING = "UTF-8";

    private final JavaMailSenderImpl javaMailSender;

    private final MessageTemplateService messageTemplateService;


    @Override
    public void sendDocProcessMessage(String to, String text, String subject) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MESSAGE_ENCODING);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn("Unable to send message: ", e);
        }

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
