package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "changes")
public class FileChanges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "changes_id")
    private final Long id;
    @Column(name = "date_of_change")
    private LocalDate date;
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    @Column(name = "previous_version", nullable = false, length = 1000)
    private String previousVersion;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
