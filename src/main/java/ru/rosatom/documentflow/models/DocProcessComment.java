package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "document_process_comment")
public class DocProcessComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private final Long id;

    @Column(name = "comment_content")
    private String content;

    @JoinColumn(name = "user_id")
    @OneToOne
    private User author;

    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @JoinColumn(name = "document_process_id")
    @ManyToOne
    private DocProcess docProcess;
}
