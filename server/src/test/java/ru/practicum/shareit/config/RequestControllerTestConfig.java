package ru.practicum.shareit.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.request.service.IRequestService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class RequestControllerTestConfig {
    @Bean
    public IRequestService requestService() {
        return mock(IRequestService.class);
    }
}
