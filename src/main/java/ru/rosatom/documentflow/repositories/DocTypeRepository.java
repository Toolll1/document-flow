package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosatom.documentflow.models.DocType;

public interface DocTypeRepository extends JpaRepository<DocType,Long> {}
