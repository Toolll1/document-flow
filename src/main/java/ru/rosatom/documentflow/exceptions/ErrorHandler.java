package ru.rosatom.documentflow.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rosatom.documentflow.dto.AppError;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> handleConflictException(final ConflictException e) {
        return createAppError(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleBadRequest(final BadRequestException e) {
        return createAppError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleWrongSortParameter(PropertyReferenceException e) {
        return createAppError(e, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<AppError> handleObjectNotFound(final ObjectNotFoundException e) {
        return createAppError(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> emptyResultDataAccessException(final EmptyResultDataAccessException e) {
        return createAppError(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleAccessDenied(final AccessDeniedException e) {
        return createAppError(e, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler
    public ResponseEntity<AppError> handleRemainingErrors(final Exception e) {
        log.error("Unhandled exception", e);
        return createAppError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<AppError> createAppError(Throwable e, HttpStatus status) {
        return new ResponseEntity<>(
                AppError.builder()
                        .message(e.getMessage())
                        .build(),
                status
        );
    }


}
