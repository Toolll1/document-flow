package ru.rosatom.documentflow.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rosatom.documentflow.models.UserPassport;
import ru.rosatom.documentflow.repositories.UserPassportRepository;
import ru.rosatom.documentflow.services.UserPassportService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPassportServiceImpl implements UserPassportService {

    private final UserPassportRepository repository;

    @Override
    public UserPassport createPassport(UserPassport passport) {

        repository.save(passport);

        return passport;
    }
}
