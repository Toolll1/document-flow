package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.dto.DocProcessDto;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.MessagePattern;

import javax.mail.MessagingException;

public interface EmailService {
  void sendDocProcessMessage(String to, String text, String subject) throws MessagingException;

  void formatMessageForRecipient(DocProcess docProcess, MessagePattern messagePattern) throws MessagingException;

  void formatMessageForSender(DocProcess docProcess, MessagePattern messagePattern) throws MessagingException;
}
