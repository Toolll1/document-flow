package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.MessagePattern;

public interface EmailService {
    void sendDocProcessMessage(String to, String text, String subject);

    void sendDocProcessAgreementMessageForRecipient(DocProcess docProcess);

    void sendDocProcessResultMessageForOwner(DocProcess docProcess, MessagePattern messagePattern);
}
