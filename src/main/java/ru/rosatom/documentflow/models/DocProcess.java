package ru.rosatom.documentflow.models;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
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

//    @Column(name = "comment", nullable = false, length = 1000)
//    private String comment;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DocProcess that = (DocProcess) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
