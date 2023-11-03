package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "organizations")
public class UserOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    private final Long id;
    @Column(name = "inn", nullable = false, length = 10)
    private final String inn;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
