package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "attribute_values")
public class DocAttributeValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "value_id")
    private Long valueId;
    @Column(name = "attribute_value", nullable = false, length = 1000)
    private String value;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private DocAttribute attribute;
}
