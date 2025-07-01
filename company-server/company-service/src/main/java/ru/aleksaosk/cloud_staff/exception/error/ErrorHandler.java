package ru.aleksaosk.cloud_staff.exception.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.aleksaosk.cloud_staff.exception.CompanyNameAlreadyInUseException;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS dd-MM-yyyy");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInvalidCompanyNameException(final CompanyNameAlreadyInUseException e) {
        log.error("CompanyNameAlreadyInUseException: Пользователь указал некорректные данные. " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.CONFLICT.toString(), "incorrect data",
                e.getReason(), now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("NotFoundException: Пользователь указал некорректные данные. " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), "incorrect data",
                e.getReason(), now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    //исключение при срабатывании аннотации на отдельных полях (переменные пути)
    public ErrorResponse handleAnnotationsField(ConstraintViolationException e) {
        String response = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        log.error("ConstraintViolationException: Пользователь указал некорректные данные. " + response);
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "incorrect data",
                response, now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //исключение при срабатывании аннотации на объектах
    public ErrorResponse handleAnnotationsObject(MethodArgumentNotValidException e) {
        String response = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("MethodArgumentNotValidException: Пользователь указал некорректные данные." + response);
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "incorrect data",
                response, now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(final UnsupportedEncodingException e) {
        log.error("UnsupportedEncodingException: Пользователь указал некорректные данные.");
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "The dates are incorrect",
                "Required format 'yyyy-MM-dd HH:mm:ss'", now.format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Throwable: Произошла ошибка на сервере. INTERNAL_SERVER_ERROR: " + e.getMessage());
        LocalDateTime now = LocalDateTime.now();
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "An error has occurred on the server",
                now.format(formatter));
    }
}