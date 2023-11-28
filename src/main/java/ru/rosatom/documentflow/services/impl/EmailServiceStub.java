package ru.rosatom.documentflow.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.services.MessageTemplateService;

@Service
@Slf4j
@Profile("dev")
public class EmailServiceStub extends AbstractEmailService {


    public EmailServiceStub(MessageTemplateService messageTemplateService) {
        super(messageTemplateService);
    }

    @Override
    public void sendDocProcessMessage(String to, String text, String subject) {
        log.info("Message sent successfully\n" +
                "From: {}\n" +
                "To: {}\n" +
                "Subject: {}\n" +
                "Body: {}\n", from, to, subject, text);
    }


}