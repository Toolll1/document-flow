package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocAttribute;

import java.util.List;

@Repository
public interface DocAttributeRepository extends JpaRepository<DocAttribute, Long> {
    List<DocAttribute> findByNameContains(String name);
}
