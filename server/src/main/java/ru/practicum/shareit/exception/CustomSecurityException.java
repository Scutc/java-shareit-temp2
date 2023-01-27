package ru.practicum.shareit.exception;

public class CustomSecurityException extends RuntimeException {
    public CustomSecurityException(String message) {
        super(message);
    }
}
