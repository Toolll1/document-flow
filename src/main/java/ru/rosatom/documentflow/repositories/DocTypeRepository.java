package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocType;

import java.util.List;

@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {
    @Query("select d from DocType d where d.userOrganization.id = ?1")
    Page<DocType> findAllByUserOrganization(Long orgId, Pageable pageable);

    List<DocType> findByNameContains(String name);

    List<DocType> findByUserOrganizationIdAndNameContains(Long userOrganizationId, String name);
}
