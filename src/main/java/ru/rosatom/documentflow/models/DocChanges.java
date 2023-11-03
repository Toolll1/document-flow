package ru.rosatom.documentflow.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "document_changes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocChanges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "changes_id")
    Long id;
    @Column(name = "document_Id")
    Long documentId;
    @Column(name = "date_of_change")
    LocalDate dateChange;
    @Column(name = "changes", nullable = false, length = 1000)
    String changes;
    @Column(name = "previous_version", nullable = false, length = 1000)
    String previousVersion;
    @JoinColumn(name = "user_Changer_Id")
    Long userChangerId;
}
