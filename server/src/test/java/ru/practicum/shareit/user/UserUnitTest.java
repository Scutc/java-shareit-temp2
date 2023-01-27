package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserUnitTest {

    IUserService userService;
    User user;
    UserDto userDto;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        user = new User(1L, "User1", "user1@mail.com");
        userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    @Test
    void getUserByIdTest() {
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        assertThat(userService.getUserById(1L).getId().equals(1L));
    }

    @Test
    void findAllUsersTest() {
        Mockito
                .when(userRepository.findAllUsers())
                .thenReturn(List.of(user));

        assertThat(userService.findAllUsers().size() == 1);
    }

    @Test
    void creteUserTestException() {
        Mockito
                .when(userRepository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateDataException.class, () -> userService.createUser(userDto));
    }

    @Test
    void createUserTest() {
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);
        assertThat(userService.createUser(userDto).getId().equals(1L));
    }

    @Test
    void updateUserTest() {
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);

        assertThat(userService.updateUser(1L, userDto).getName().equals("User1"));
    }
}
