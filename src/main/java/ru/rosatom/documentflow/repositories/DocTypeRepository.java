package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocType;

@Repository

public interface DocTypeRepository extends JpaRepository<DocType, Long> {
}
