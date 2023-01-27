package ru.practicum.shareit.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@TestConfiguration // помечает класс как java-config для контекста приложения
@EnableWebMvc  // призывает импортировать дополнительную конфигурацию для веб-приложений
public class WebConfig {
}