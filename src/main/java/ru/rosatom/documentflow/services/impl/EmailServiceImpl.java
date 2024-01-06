package ru.rosatom.documentflow.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.services.MessageTemplateService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@Profile("prod")
public class EmailServiceImpl extends AbstractEmailService {

    private final String MESSAGE_ENCODING = "UTF-8";

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        return new JavaMailSenderImpl();
    }

    private final JavaMailSenderImpl javaMailSender;

    public EmailServiceImpl(MessageTemplateService messageTemplateService, JavaMailSenderImpl javaMailSender) {
        super(messageTemplateService);
        this.javaMailSender = javaMailSender;
    }


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


}
