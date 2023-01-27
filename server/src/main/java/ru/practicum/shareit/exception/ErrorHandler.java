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
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleDuplicateDataException(DuplicateDataException e, HttpServletRequest request) {
        log.warn("Дублирующиеся данные {} по пути запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleSecurityException(CustomSecurityException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleNotAvailableException(NotAvailableException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<String> handleNotValidDataException(NotValidDateException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnsupportedStatusException(UnsupportedStatusException e) {
        log.warn("Unknown state: UNSUPPORTED_STATUS");
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotAllowedToChangeException(NotAllowedToChangeException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotValidParamsException(NotValidParamsException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleRequestNotFoundException(RequestNotFoundException e, HttpServletRequest request) {
        log.warn("{}. Путь запроса {}", e.getMessage(), request.getServletPath());
        return new ResponseEntity<>(e.getMessage() + " Путь запроса: "
                + request.getServletPath(), HttpStatus.NOT_FOUND);
    }
}
