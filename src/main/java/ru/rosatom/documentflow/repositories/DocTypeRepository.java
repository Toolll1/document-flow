package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;

@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {
    Page<DocType> findAllByUserOrganization(UserOrganization userOrganization, Pageable pageable);

    List<DocType> findByNameContains(String name);
}
