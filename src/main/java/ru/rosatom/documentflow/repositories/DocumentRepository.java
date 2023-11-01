package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.Document;

import java.util.Set;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, QuerydslPredicateExecutor<Document> {
    @Query("select * from documents d " +
            "left join document_process dp on dp.document_id=d.document_id " +
            "where dp.status = :status")
    Set<Document> findDocumentsByProcessStatus(@Param("status") DocProcessStatus status);
}
