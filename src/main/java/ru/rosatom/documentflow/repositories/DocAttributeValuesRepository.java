package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.rosatom.documentflow.models.DocAttributeValues;

@Repository
public interface DocAttributeValuesRepository extends JpaRepository<DocAttributeValues, Long>,
        QuerydslPredicateExecutor<DocAttributeValues> {
}
