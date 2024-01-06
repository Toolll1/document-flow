package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.MessagePattern;
import ru.rosatom.documentflow.models.MessageTemplate;

public interface MessageTemplateService {

    MessageTemplate getMessageTemplateByPattern(MessagePattern messagePattern);

}
