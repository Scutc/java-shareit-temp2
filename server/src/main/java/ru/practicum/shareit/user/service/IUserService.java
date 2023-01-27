package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface IUserService {
    UserDto getUserById(Long userId);

    List<UserDto> findAllUsers();

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    boolean deleteUser(Long userId);
}
