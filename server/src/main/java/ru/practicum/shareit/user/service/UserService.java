package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.service.UserMapper.toUser;
import static ru.practicum.shareit.user.service.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Получение пользователя по ID = {}", userId);
        User user = userRepository.getUserById(userId);
        if (user != null) {
            return toUserDto(user);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAllUsers();
        return users.stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Создание нового пользователя {}", userDto);
        try {
            User user = userRepository.save(toUser(userDto));
            return toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("Пользователь с такими данными уже существует в базе!");
            throw new DuplicateDataException("Пользователь с такими данными уже существует в базе!");
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Обновление пользователя с ID = {}", userId);
        User userForUpdate = userRepository.getUserById(userId);
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userForUpdate.setEmail(userDto.getEmail());
        }
        try {
            userRepository.save(userForUpdate);
            return toUserDto(userForUpdate);
        } catch (DataIntegrityViolationException e) {
            log.warn("Пользователь с такими данными уже существует в базе!");
            throw new DuplicateDataException("Пользователь с такими данными уже существует в базе!");
        }
    }

    @Transactional
    @Override
    public boolean deleteUser(Long userId) {
        try {
            User userForDelete = userRepository.getUserById(userId);
            userRepository.delete(userForDelete);
            log.info("Удаление пользователя ID = {} успешно", userId);
            return true;
        } catch (UserNotFoundException e) {
            log.info("Удаление пользователя ID = {} не удалось, пользователь не найден", userId);
            return false;
        }
    }
}
