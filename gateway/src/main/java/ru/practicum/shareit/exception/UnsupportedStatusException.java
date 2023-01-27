package ru.practicum.shareit.exception;

public class UnsupportedStatusException extends RuntimeException {
    public UnsupportedStatusException(String state) {
        super("Unknown state: " + state);
    }
}
