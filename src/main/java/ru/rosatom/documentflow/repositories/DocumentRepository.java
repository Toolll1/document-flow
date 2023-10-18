package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosatom.documentflow.models.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
