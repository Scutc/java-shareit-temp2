package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Поступил запрос на получение пользователя с ID {}", userId);
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("Поступил запрос на получение всех пользователей");
        return userClient.findAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object>  createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Поступил запрос на создание пользователя {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object>  updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Поступил запрос на обновление пользователя с ID = {}", userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Поступил запрос на обновление пользователя с ID = {}", userId);
        return userClient.deleteUser(userId);
    }
}
