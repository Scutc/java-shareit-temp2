package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("Ошибка валидации полей объекта: {} {}. Путь запроса {}", e.getFieldError().getField(),
                e.getFieldError().getDefaultMessage(), request.getServletPath());
        return new ResponseEntity<>("Ошибка валидации полей объекта: " + e.getFieldError().getField()
                + " " + e.getFieldError().getDefaultMessage() + ". Путь запроса "
                + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<String> handleConstraintViolationExceptionException(ConstraintViolationException e, HttpServletRequest request) {
        log.warn("Ошибка валидации полей объекта: {} {}. Путь запроса {}", e.getConstraintViolations(),
                e.getLocalizedMessage(), request.getServletPath());
        return new ResponseEntity<>("Ошибка валидации полей объекта: " + e.getLocalizedMessage()
                + " " + e.getLocalizedMessage() + ". Путь запроса "
                + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedStatusException(UnsupportedStatusException e) {
        log.warn("Unknown state: UNSUPPORTED_STATUS");
        return Map.of("error", e.getMessage());
    }
}
