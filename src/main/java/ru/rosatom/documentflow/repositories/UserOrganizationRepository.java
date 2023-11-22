package ru.rosatom.documentflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rosatom.documentflow.models.UserOrganization;

import java.util.List;
import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long> {

    Optional<UserOrganization> findByName(String name);

    List<UserOrganization> findByNameContainsIgnoreCase(String name);

    @Query("select org from UserOrganization org " +
            "inner join User u on u.organization.id=org.id " +
            "inner join Document d on u.id=d.ownerId " +
            "group by org.name " +
            "order by count(org.name) desc")
    List<UserOrganization> findActiveOrganization();

}
