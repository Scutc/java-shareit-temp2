package ru.practicum.shareit.exception;

public class NotValidDateException extends RuntimeException {
    public NotValidDateException() {
        super("Некорретно заданы даты бронирования");
    }
}
