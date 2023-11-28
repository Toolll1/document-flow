package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "message_templates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String body;

    @Enumerated(EnumType.STRING)
    private MessagePattern messagePattern;

    private String subject;

}
