package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocAttribute;

import java.util.List;

@Repository
public interface DocAttributeRepository extends JpaRepository<DocAttribute, Long> {
    List<DocAttribute> findByNameContains(String name);

    @Query("select d from DocAttribute d where d.userOrganization.id = ?1")
    Page<DocAttribute> findAllByUserOrganization(Long orgId, Pageable pageable);
}
