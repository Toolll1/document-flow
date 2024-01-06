package ru.rosatom.documentflow.models;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
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

    public String getFullName(){
        StringBuilder fullName = new StringBuilder();

        fullName.append(getFirstName());

        if (getPatronymic() != null) {
            fullName.append(" ").append(getPatronymic());
        }

        fullName.append(" ").append(getLastName());

        return fullName.toString();
    }
    public boolean isAdmin() {
        return this.getRole() == UserRole.ADMIN;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
