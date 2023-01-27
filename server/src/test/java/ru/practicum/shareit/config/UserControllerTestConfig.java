package ru.practicum.shareit.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.user.service.IUserService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class UserControllerTestConfig {
    @Bean
    public IUserService userService() {
        return mock(IUserService.class);
    }
}