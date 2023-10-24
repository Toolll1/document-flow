package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocAttribute;

import java.util.List;

@Repository
public interface DocAttributeRepository extends JpaRepository<DocAttribute, Long> {
  @Override
  Page<DocAttribute> findAll(Pageable pageable);
  @Override
  List<DocAttribute> findAll(Sort sort);
}
