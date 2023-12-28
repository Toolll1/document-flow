package ru.rosatom.documentflow.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
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
