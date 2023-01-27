package ru.practicum.shareit.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.booking.service.IBookingService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class BookingControllerTestConfig {
    @Bean
    public IBookingService bookingService() {
        return mock(IBookingService.class);
    }
}
