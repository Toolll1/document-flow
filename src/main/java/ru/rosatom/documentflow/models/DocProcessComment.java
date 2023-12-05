package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Builder
@Table(name = "document_process_comment")
public class DocProcessComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "text_comment")
    private String textComment;

    @JoinColumn(name = "user_id")
    @OneToOne
    private User authorComment;

    @Column (name = "date_comment")
    private LocalDateTime date;

    @JoinColumn(name = "document_id")
    @ManyToOne
    private Document document;
}
