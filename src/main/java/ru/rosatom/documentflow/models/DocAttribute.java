package ru.rosatom.documentflow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;

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
  @Column(name = "type", nullable = false, length = 16)
  private String type;

  @JsonIgnore
  @ManyToMany(mappedBy = "attributes")
  private List<DocType> docTypes = new ArrayList<>();
}
