package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosatom.documentflow.models.UserOrganization;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long> {
}
