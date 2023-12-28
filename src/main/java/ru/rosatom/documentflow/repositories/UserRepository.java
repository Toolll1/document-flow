package ru.rosatom.documentflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rosatom.documentflow.dto.UserRatingDto;
import ru.rosatom.documentflow.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByIdIn(List<Long> ids, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByPassportSeriesAndPassportNumber(String passportSeries, String passportNumber);

    @Query("select new ru.rosatom.documentflow.dto.UserRatingDto(u.id, u.lastName, u.firstName, u.email, count(d)) " +
            "from User u join Document d on u.id = d.ownerId " +
            "where u.organization.id = :organizationId group by u")
    List<UserRatingDto> findRatingForAllUsersByOrganizationId(Long organizationId);

    List<User> findAllByOrganizationId(Long id);
}
