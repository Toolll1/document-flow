package ru.rosatom.documentflow.models;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "document_types")
public class DocType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private final Long id;

    @Column(name = "name", nullable = false, length = 320)
    private String name;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, targetEntity = DocAttribute.class)
    @JoinTable(
            name = "type_attributes",
            joinColumns = @JoinColumn(name = "type_id", referencedColumnName = "type_id"),
            inverseJoinColumns =
            @JoinColumn(name = "attribute_id", referencedColumnName = "attribute_id"))
    private Set<DocAttribute> attributes = new HashSet<>();

    @Column(name = "agreement_type", length = 16)
    @Enumerated(EnumType.STRING)
    private AgreementType agreementType;

    @OneToOne(targetEntity = UserOrganization.class)
    @JoinColumn(name = "org_id")
    private UserOrganization userOrganization;

    public void addAttributes(DocAttribute docAttribute) {
        attributes.add(docAttribute);
    }

    public boolean containsAttribute(DocAttribute docAttribute){
        return attributes.contains(docAttribute);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DocType docType = (DocType) o;
        return getId() != null && Objects.equals(getId(), docType.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
