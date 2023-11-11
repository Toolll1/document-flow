package ru.rosatom.documentflow.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
}
