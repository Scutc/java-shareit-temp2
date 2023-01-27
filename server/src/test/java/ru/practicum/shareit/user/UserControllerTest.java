package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.config.UserControllerTestConfig;
import ru.practicum.shareit.config.WebConfig;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringJUnitWebConfig({ UserController.class, UserControllerTestConfig.class, WebConfig.class, ErrorHandler.class})
public class UserControllerTest {
    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new ErrorHandler(), controller)
                .build();

        userDto = new UserDto(1L, "User1", "user1@email.ru");
    }

    @Test
    void createUserTest() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                   .content(mapper.writeValueAsString(userDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
           .andExpect(jsonPath("$.name", is(userDto.getName())))
           .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        when(userService.createUser(any()))
                .thenThrow(DuplicateDataException.class);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(userService.getUserById(any()))
                .thenReturn(userDto);

        mvc.perform(get("/users/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
           .andExpect(jsonPath("$.name", is(userDto.getName())))
           .andExpect(jsonPath("$.email", is(userDto.getEmail())));

    }

    @Test
    void getUserByIdTestException() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenThrow(UserNotFoundException.class);

        mvc.perform(get("/users/1")
                   .content(mapper.writeValueAsString(userDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
    }

    @Test
    void getUserWithEntityNotFoundException() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/users/1")
                   .content(mapper.writeValueAsString(userDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
    }

    @Test void getAllUsersTest() throws Exception {
        when(userService.findAllUsers())
                .thenReturn(List.of(userDto));

        mvc.perform(get("/users"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class))
           .andExpect(jsonPath("$[0].name", is(userDto.getName())))
           .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));
    }

    @Test void updateUserTest() throws Exception {
        UserDto userDtoAfterUpdate = userDto;
        userDtoAfterUpdate.setName("after update");
        when(userService.updateUser(any(), any()))
                .thenReturn(userDtoAfterUpdate);

        mvc.perform(patch("/users/1")
           .content(mapper.writeValueAsString(userDto))
           .characterEncoding(StandardCharsets.UTF_8)
           .contentType(MediaType.APPLICATION_JSON)
           .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(userDtoAfterUpdate.getId()), Long.class))
           .andExpect(jsonPath("$.name", is(userDtoAfterUpdate.getName())))
           .andExpect(jsonPath("$.email", is(userDtoAfterUpdate.getEmail())));
    }

    @Test void deleteUserTest() throws Exception {
        when(userService.deleteUser(any()))
                .thenReturn(true);

        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}
