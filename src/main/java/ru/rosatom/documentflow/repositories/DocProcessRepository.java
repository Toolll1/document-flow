package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;
import ru.rosatom.documentflow.models.Document;

import java.util.Collection;
import java.util.Set;

@Repository
public interface DocProcessRepository extends CrudRepository<DocProcess, Long> {

    Collection<DocProcess> findAllByDocumentId(Long documentId);

    @Query("select * from documents d " +
            "left join document_process dp on dp.document_id=d.document_id " +
            "where dp.status = :status")
    Set<Document> findDocumentsByProcessStatus(@Param("status") DocProcessStatus status);
}
