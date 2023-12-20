package ru.rosatom.documentflow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "attributes")
public class DocAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private final Long id;

    @Column(name = "name", nullable = false, length = 320)
    private String name;

    @Column(name = "type", nullable = false, length = 500)
    private String type;


    @JsonIgnore
    @ManyToMany(mappedBy = "attributes")
    private List<DocType> docTypes = new ArrayList<>();

    @OneToOne(targetEntity = UserOrganization.class)
    @JoinColumn(name = "org_id")
    private UserOrganization organization;
}
