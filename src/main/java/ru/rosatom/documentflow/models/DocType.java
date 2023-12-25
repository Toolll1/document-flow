package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
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
}
