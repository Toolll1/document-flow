package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("select d from Document d " +
            "left join DocProcess dp on dp.document.id=d.id " +
            "where dp.status = :status")
    Set<Document> findDocumentsByProcessStatus(@Param("status") DocProcessStatus status);

    @Query("select d from Document d " +
            "left join DocProcess dp on dp.document.id=d.id " +
            "where dp.status = :status and d.idOrganization = :id")
    Set<Document> findDocumentsByProcessStatusAndIdOrganization(@Param("status") DocProcessStatus status, @Param("id")Long id);

    Page<Document> findAllByIdOrganization(Long idOrganization, Pageable pageable);

}
