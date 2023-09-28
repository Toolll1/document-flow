package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "file_types")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private final Long id;
    @Column(name = "name", nullable = false, length = 320)
    private  String name;
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Attribute.class)
    @JoinTable(name = "type_attributes",
            joinColumns = @JoinColumn(name = "type_id", referencedColumnName = "type_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id", referencedColumnName = "attribute_id"))
    private List<Attribute> attributes;
}
