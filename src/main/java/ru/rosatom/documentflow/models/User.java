package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private final Long id;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    @Column(name = "patronymic", length = 100)
    private String patronymic;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "email", nullable = false, length = 320, unique = true)
    private String email;
    @Column(name = "phone", nullable = false, length = 11, unique = true)
    private String phone;
    @Column(name = "user_password")
    private String password;
    @Column(name = "post", nullable = false, length = 320)
    private String post;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "passport_id")
    private UserPassport passport;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "org_id")
    private UserOrganization organization;
}
