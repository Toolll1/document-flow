package ru.rosatom.documentflow.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    final Long id;
    @Column(name = "title", nullable = false, length = 256)
    String title;
    @Column(name = "document_path", nullable = false, length = 1000)
    String documentPath;
    @Column(name = "created_at")
    LocalDateTime date;
    @Column(name = "organization_id")
    Long idOrganization;
    @ToString.Exclude
    @JoinColumn(name = "owner_Id")
    Long ownerId;  //создатель файла
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "type_id")
    DocType docType;
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    List<DocAttributeValues> attributeValues = new ArrayList<>();
}