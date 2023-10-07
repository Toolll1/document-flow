package ru.rosatom.documentflow.mappers;

import org.springframework.stereotype.Service;
import ru.rosatom.documentflow.adapters.DateTimeAdapter;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.models.UserPassport;

@Service
public class UserPassportMapper {

    public UserPassport dtoToObject(UserCreateDto dto) {

        return UserPassport.builder()
                .series(dto.getPassportSeries())
                .number(dto.getPassportNumber())
                .issued(dto.getPassportIssued())
                .date(DateTimeAdapter.stringToDate(dto.getPassportDate()))
                .kp(dto.getPassportKp())
                .build();
    }
}
