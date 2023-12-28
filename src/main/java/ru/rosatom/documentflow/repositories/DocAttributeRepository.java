package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocAttribute;

import java.util.List;
import java.util.Set;

@Repository
public interface DocAttributeRepository extends JpaRepository<DocAttribute, Long> {
    List<DocAttribute> findByNameContains(String name);

    @Query("select d from DocAttribute d where d.organization.id = ?1")
    Page<DocAttribute> findAllByUserOrganization(Long orgId, Pageable pageable);

    List<DocAttribute> findByOrganizationIdAndNameContains(Long organizationId, String name);

    @Query("SELECT DISTINCT a FROM DocAttribute a WHERE a.id IN :ids")
    Set<DocAttribute> findDistinctByIds(@Param("ids") Set<Long> ids);

}
