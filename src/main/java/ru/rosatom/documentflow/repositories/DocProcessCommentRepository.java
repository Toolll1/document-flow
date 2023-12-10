package ru.rosatom.documentflow.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocProcessComment;
import java.util.List;

@Repository
public interface DocProcessCommentRepository extends JpaRepository<DocProcessComment, Long> {

    List<DocProcessComment> findAllByDocumentId(Long documentId);
}