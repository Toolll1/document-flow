package ru.rosatom.documentflow.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosatom.documentflow.models.DocProcessComment;

public interface DocProcessCommentRepository extends JpaRepository<DocProcessComment, Long> {

}