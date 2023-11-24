package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.MessagePattern;
import ru.rosatom.documentflow.models.MessageTemplate;

import java.util.Optional;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
    Optional<MessageTemplate> findMessageTemplateByMessagePattern(MessagePattern messagePattern);
}