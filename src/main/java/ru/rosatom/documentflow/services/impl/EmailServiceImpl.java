package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final String MESSAGE_ENCODING = "UTF-8";

    private final JavaMailSenderImpl javaMailSender;

    private final MessageTemplateService messageTemplateService;


    @Override
    public void sendDocProcessMessage(String to, String text, String subject)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MESSAGE_ENCODING);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void formatMessageForRecipient(DocProcess docProcess, MessagePattern messagePattern) throws MessagingException {
        MessageTemplate messageTemplate = messageTemplateService.getMessageTemplateByPattern(MessagePattern.AGREEMENT);
        String text = String.format(messageTemplate.getBody(),
                docProcess.getRecipient().getFirstName(),
                docProcess.getDocument().getName(),
                docProcess.getSender().getEmail(),
                docProcess.getSender().getLastName() + ' ' + docProcess.getSender().getFirstName() + ' ' + docProcess.getSender().getPatronymic());
        sendDocProcessMessage(text, docProcess.getRecipient().getEmail(), messageTemplate.getSubject());
    }

    @Override
    public void formatMessageForSender(DocProcess docProcess, MessagePattern messagePattern) throws MessagingException {
        MessageTemplate messageTemplate = messageTemplateService.getMessageTemplateByPattern(messagePattern);
        String text = String.format(messageTemplate.getBody(),
                docProcess.getSender().getFirstName(),
                docProcess.getDocument().getName());
        sendDocProcessMessage(text, docProcess.getSender().getEmail(), messageTemplate.getSubject());

    }


}
