package ru.rosatom.documentflow.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocProcess;

@Repository
public interface DocProcessRepository extends CrudRepository<DocProcess, Long> {
}
