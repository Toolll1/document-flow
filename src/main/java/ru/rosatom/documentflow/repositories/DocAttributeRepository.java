package ru.rosatom.documentflow.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocAttribute;

@Repository
public interface DocAttributeRepository extends JpaRepository<DocAttribute, Long> {
  List<DocAttribute> findByNameContains(String name);
}
