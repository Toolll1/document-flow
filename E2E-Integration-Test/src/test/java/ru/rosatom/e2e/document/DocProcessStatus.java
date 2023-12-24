package ru.rosatom.e2e.document;

public enum DocProcessStatus {
    NEW,
    WAITING_FOR_APPROVE,
    APPROVED,
    REJECTED,
    CORRECTING,
    DELEGATED
}
