package ru.practicum.shareit.exception;

public class NotAllowedToChangeException extends RuntimeException {
    public NotAllowedToChangeException(String message) {
        super(message);
    }
}
