package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;
import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long> {

    Optional<UserOrganization> findByName(String name);

    List<UserOrganization> findByNameContains(String name);

}
