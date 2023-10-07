package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "attribute_values")
public class FileAttributeValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "value_id")
    private final Long id;
    @Column(name = "attribute_name", nullable = false, length = 320)
    private String name;
    @Column(name = "attribute_value", nullable = false, length = 1000)
    private String value;
}
