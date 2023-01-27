package ru.practicum.shareit.exception;

public class NotAvailableException extends RuntimeException {
    public NotAvailableException(Long itemId) {
        super("Товар с ID = " + itemId + " недоступен для бронирования");
    }
}
