package ru.rosatom.documentflow.services;

import ru.rosatom.documentflow.models.UserPassport;

public interface UserPassportService {
    UserPassport createPassport(UserPassport passport);
}
