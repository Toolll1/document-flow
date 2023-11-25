package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocType;

import java.util.List;

@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {
    List<DocType> findByNameContains(String name);
}
