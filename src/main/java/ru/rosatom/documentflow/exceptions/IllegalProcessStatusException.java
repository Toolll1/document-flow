package ru.rosatom.documentflow.exceptions;

import ru.rosatom.documentflow.models.DocProcess;
import ru.rosatom.documentflow.models.DocProcessStatus;

public class IllegalProcessStatusException extends BadRequestException {
    public IllegalProcessStatusException(DocProcess docProcess, DocProcessStatus attemptStatus) {
        super("Нельзя перевести процесс со статусом %s в статус %s".formatted(docProcess.getStatus(), attemptStatus));
    }
}
