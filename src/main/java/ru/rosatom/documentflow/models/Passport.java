package ru.rosatom.documentflow.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "passports")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private final Long id;
    @Column(name = "series", nullable = false, length = 4)
    private  String series;
    @Column(name = "number", nullable = false, length = 6)
    private  String number;
    @Column(name = "issued", nullable = false, length = 320)
    private  String issued; //кем выдан
    @Column(name = "date")
    private  LocalDate date;
    @Column(name = "kp", nullable = false, length = 6)
    private  String kp;  //код подразделения
}
