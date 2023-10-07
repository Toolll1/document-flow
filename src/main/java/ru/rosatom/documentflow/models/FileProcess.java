package ru.rosatom.documentflow.models;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "file_process")
public class FileProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private final Long id;
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
    private FileProcessStatus status;
    @Column(name = "comment", nullable = false, length = 1000)
    private String comment;
}
