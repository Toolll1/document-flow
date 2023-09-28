package ru.rosatom.documentflow.adapters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeAdapter {

    public static String dateToString(LocalDate date) {

        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static LocalDate stringToDate(String date) {

        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
