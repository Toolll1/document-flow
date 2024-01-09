package ru.rosatom.documentflow.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
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
    @OneToMany
    @JoinColumn(name = "document_id")
    List<DocAttributeValues> attributeValues = new ArrayList<>();

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "final_doc_status")
    DocProcessStatus finalDocStatus;

    @Column(name = "comments")
    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<DocProcessComment> comments;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Document document = (Document) o;
        return getId() != null && Objects.equals(getId(), document.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}