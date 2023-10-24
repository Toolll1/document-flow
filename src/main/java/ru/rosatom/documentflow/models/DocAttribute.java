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
@Table(name = "attributes")
public class DocAttribute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "attribute_id")
  private final Long id;

  @Column(name = "name", nullable = false, length = 320)
  private String name;

  @Column(name = "type", nullable = false, length = 16)
  private String type;
}
