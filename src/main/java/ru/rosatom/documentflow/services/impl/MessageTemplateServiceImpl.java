package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.exceptions.ObjectNotFoundException;
import ru.rosatom.documentflow.models.MessagePattern;
import ru.rosatom.documentflow.models.MessageTemplate;
import ru.rosatom.documentflow.repositories.MessageTemplateRepository;
import ru.rosatom.documentflow.services.MessageTemplateService;

@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl implements MessageTemplateService {
    private final MessageTemplateRepository messageTemplateRepository;

    @Override
    public MessageTemplate getMessageTemplateByPattern(MessagePattern messagePattern) {
        return messageTemplateRepository.findMessageTemplateByMessagePattern(messagePattern)
                .orElseThrow(() -> new ObjectNotFoundException("Message template not found"));
    }
}
