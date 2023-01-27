package ru.practicum.shareit.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long requestId) {
        super("Запрос на бронирование с ID = " + requestId + " не найден!");
    }
}
