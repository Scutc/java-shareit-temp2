package ru.practicum.shareit.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.item.service.IItemService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ItemControllerTestConfig {
    @Bean
    public IItemService itemService() {
        return mock(IItemService.class);
    }
}
