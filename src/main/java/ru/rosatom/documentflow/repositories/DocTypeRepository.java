package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocType;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {
  List<DocType> findByNameContains(String name);
}
