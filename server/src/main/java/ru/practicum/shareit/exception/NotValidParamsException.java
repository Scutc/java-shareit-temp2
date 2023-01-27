package ru.practicum.shareit.exception;

public class NotValidParamsException extends RuntimeException {
    public NotValidParamsException(String message) {
        super(message);
    }
}
