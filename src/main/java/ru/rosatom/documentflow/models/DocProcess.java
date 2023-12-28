package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
}
