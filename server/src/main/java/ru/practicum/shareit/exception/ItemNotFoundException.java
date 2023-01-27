package ru.practicum.shareit.exception;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(Long itemId) {
        super("Товар с ID " + itemId + " не найден");
    }
}
