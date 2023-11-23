package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "document_process")
public class DocProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private final Long id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "document_id")
    private final Document document;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "sender_id")
    private User sender;  //отправитель
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;  //получатель
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocProcessStatus status;

    @Column(name = "comment")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private List<DocProcessComment> comment;

}
