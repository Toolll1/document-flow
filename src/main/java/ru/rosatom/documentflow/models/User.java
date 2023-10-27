package ru.rosatom.documentflow.models;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

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
    @Column(name = "user_password", nullable = false, length = 255)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
