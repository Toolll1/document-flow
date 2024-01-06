package ru.rosatom.e2e.user;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Passport {
    private String passportSeries;
    private String passportNumber;
    private String passportIssued;
    private String passportDate;
    private String passportKp;

}
