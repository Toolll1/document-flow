package ru.rosatom.documentflow.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @Column(name = "name", nullable = false, length = 256)
    String name;
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
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "final_doc_status")
    DocProcessStatus finalDocStatus;
}