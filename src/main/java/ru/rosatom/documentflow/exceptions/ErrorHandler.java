package ru.rosatom.documentflow.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.rosatom.documentflow.dto.AppError;
import ru.rosatom.documentflow.dto.InvalidIdsError;
import ru.rosatom.documentflow.dto.ValidationError;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AppError> handleAuthenticationException(final AuthenticationException e) {
        return createAppError(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleWrongSortParameter(PropertyReferenceException e) {
        return createAppError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return createAppError(e, HttpStatus.BAD_REQUEST);
    }

  
    public ResponseEntity<AppError> handleDateTimeParseException(final DateTimeParseException e) {
        return createAppError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return createValidationError(e, HttpStatus.CONFLICT);
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
    public ResponseEntity<AppError> handleBadCredentialsException(final BadCredentialsException e){
        return createAppError(e, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler
    public ResponseEntity<AppError> handleRemainingErrors(final Exception e) {
        log.error("Unhandled exception", e);
        return createAppError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleUnprocessableEntityException(final UnprocessableEntityException e) {
        return createAppError(e, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler
    public ResponseEntity<AppError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException){
        String errorMessage = String.format("Parameter %s is invalid",
                methodArgumentTypeMismatchException.getValue());
        return createAppError(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<? extends AppError> handleSomeObjectInArrayNotFoundException(SomeIdsInArrayNotFoundException e) {
        return createWithCustomAppError(
                new InvalidIdsError(e.getMessage(), e.getNotFoundIds()),
                HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<AppError> createAppError(Throwable e, HttpStatus status) {
        return new ResponseEntity<>(
                AppError.builder()
                        .message(e.getMessage())
                        .build(),
                status
        );
    }

    private ResponseEntity<? extends AppError> createWithCustomAppError(AppError appError, HttpStatus status) {
        return new ResponseEntity<>(
                appError,
                status
        );
    }

    private ResponseEntity<AppError> createAppError(String message, HttpStatus status){
        return new ResponseEntity<>(
                AppError.builder()
                        .message(message)
                        .build(),
                status
        );
    }
    private ResponseEntity<ValidationError> createValidationError(BindException e, HttpStatus status) {
        Map<String, String> fieldErrors = e
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (errorMsg1, errorMsg2) -> errorMsg1));
        return new ResponseEntity<>(
                ValidationError
                        .builder()
                        .message(e.getLocalizedMessage())
                        .fieldErrors(fieldErrors)
                        .build(),
                status
        );
    }


}
