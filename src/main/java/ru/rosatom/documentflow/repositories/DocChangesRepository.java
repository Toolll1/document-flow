package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rosatom.documentflow.models.DocChanges;

import java.util.List;

public interface DocChangesRepository extends JpaRepository<DocChanges, Long> {
    Page<DocChanges> findAllByDocumentId(Long documentId, Pageable pageable);

    @Query("select d from DocChanges dc join Document d on (?1=d.id) where d.idOrganization=?2")
    Page<DocChanges> findAllByDocumentIdAndOrgId(Long documentId, Long orgId, Pageable pageable);

    List<DocChanges> findAllByUserChangerId(Long ChangerId);
}
