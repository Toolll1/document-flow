package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private final Long id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "file_path", nullable = false, length = 1000)
    private String path;
    @Column(name = "created_at")
    private LocalDate date;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "creator_id")
    private User owner;  //создатель файла
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "type_id")
    private FileType fileType;  //тип файла
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private List<FileChanges> changes = new ArrayList<>(); //список изменений
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private List<FileAttributeValues> attributeValues = new ArrayList<>();  // список значений атрибутов
}
