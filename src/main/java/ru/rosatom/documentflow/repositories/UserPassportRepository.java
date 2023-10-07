package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosatom.documentflow.models.UserPassport;

public interface UserPassportRepository extends JpaRepository<UserPassport, Long> {
}
