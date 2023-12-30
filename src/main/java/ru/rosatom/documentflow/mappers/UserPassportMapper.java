package ru.rosatom.documentflow.mappers;

import org.springframework.stereotype.Component;
import ru.rosatom.documentflow.dto.UserCreateDto;
import ru.rosatom.documentflow.models.UserPassport;

@Component
public class UserPassportMapper {

    public UserPassport dtoToObject(UserCreateDto dto) {

        return UserPassport.builder()
                .series(dto.getPassportSeries())
                .number(dto.getPassportNumber())
                .issued(dto.getPassportIssued())
                .date(dto.getPassportDate())
                .kp(dto.getPassportKp())
                .build();
    }
}
