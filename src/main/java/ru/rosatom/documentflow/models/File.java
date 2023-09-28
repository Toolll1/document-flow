package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    private  String title;
    @Column(name = "file_path", nullable = false, length = 100)
    private  String path;
    @Column(name = "created_at")
    private LocalDate date;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;  //создатель файла
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "type_id")
    private Type type;  //тип файла
}
