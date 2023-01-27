package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Поступил запрос на получение пользователя с ID {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        log.info("Поступил запрос на получение всех пользователей");
        return userService.findAllUsers();
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Поступил запрос на создание пользователя {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Поступил запрос на обновление пользователя с ID = {}", userId);
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable Long userId) {
        log.info("Поступил запрос на обновление пользователя с ID = {}", userId);
        return userService.deleteUser(userId);
    }
}
