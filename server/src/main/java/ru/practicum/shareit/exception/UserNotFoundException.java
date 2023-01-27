package ru.practicum.shareit.exception;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long userId) {
        super("Пользователь с ID " + userId + " не найден");
    }
}
