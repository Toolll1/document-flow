package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "document_process")
public class DocProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id", nullable = false)
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
    private User recipientUser;  //получатель

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "org_id")
    private UserOrganization recipientOrganization;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocProcessStatus status;

    @Column(name = "comment", nullable = false, length = 1000)
    private String comment;
}
