package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosatom.documentflow.models.DocChanges;

import java.util.List;

public interface DocChangesRepository extends JpaRepository<DocChanges, Long> {
    List<DocChanges> findAllByDocumentId(Long documentId);

    List<DocChanges> findAllByUserChangerId(Long ChangerId);
}
