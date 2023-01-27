package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.IBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.shareit.config.BookingControllerTestConfig;
import ru.practicum.shareit.config.WebConfig;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.NotValidDateException;
import ru.practicum.shareit.exception.NotValidParamsException;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({BookingController.class, BookingControllerTestConfig.class, WebConfig.class, Exception.class})
public class BookingControllerTest {
    @Mock
    private IBookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private BookingDtoResponse bookingDtoResponse;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new ErrorHandler(), controller)
                .build();

        bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                BookingStatus.WAITING, 1L, 1L);
        bookingDtoResponse = new BookingDtoResponse(1L, bookingDto.getStart(), bookingDto.getEnd(),
                bookingDto.getBookingStatus(), null, null);
    }

    @Test
    void createBookingTest() throws Exception {
        when(bookingService.createBooking(any(), any()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                   .header("X-Sharer-User-Id", 1L)
                   .content(mapper.writeValueAsString(bookingDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
           .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
           .andExpect(jsonPath("$.start", is(notNullValue())))
           .andExpect(jsonPath("$.end", is(notNullValue())));

        when(bookingService.createBooking(any(), any()))
                .thenThrow(NotValidDateException.class);

        mvc.perform(post("/bookings")
                   .header("X-Sharer-User-Id", 1L)
                   .content(mapper.writeValueAsString(bookingDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingTest() throws Exception {
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);
        when(bookingService.updateBookingStatus(1L, 1L, true))
                .thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/1")
                   .header("X-Sharer-User-Id", 1L)
                   .param("approved", "true")
                   .content(mapper.writeValueAsString(bookingDto))
                   .characterEncoding(StandardCharsets.UTF_8)
                   .contentType(MediaType.APPLICATION_JSON)
                   .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
           .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
           .andExpect(jsonPath("$.start", is(notNullValue())))
           .andExpect(jsonPath("$.end", is(notNullValue())));
    }

    @Test
    void getBookingByIdtest() throws Exception {
        when(bookingService.getBookingById(any(), any()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
           .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
           .andExpect(jsonPath("$.start", is(notNullValue())))
           .andExpect(jsonPath("$.end", is(notNullValue())));
    }

    @Test
    void getBookingByOwnerTest() throws Exception {
        when(bookingService.getBookingByOwner(any(), any(), any(), any()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Long.class))
           .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.getStatus().toString())))
           .andExpect(jsonPath("$[0].start", is(notNullValue())))
           .andExpect(jsonPath("$[0].end", is(notNullValue())));
    }

    @Test
    void getAllBookingsTest() throws Exception {
        when(bookingService.getBookingsByUser(any(), any(), any(), any()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Long.class))
           .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.getStatus().toString())))
           .andExpect(jsonPath("$[0].start", is(notNullValue())))
           .andExpect(jsonPath("$[0].end", is(notNullValue())));
    }

    @Test
    void getAllBookingsTestException() throws Exception {
        when(bookingService.getBookingsByUser(any(), any(), any(), any()))
                .thenThrow(new UnsupportedStatusException("STATUSBAD"));
        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBookingsTestException2() throws Exception {
        when(bookingService.getBookingsByUser(any(), any(), any(), any()))
                .thenThrow(NotValidParamsException.class);
        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 1L))
           .andExpect(status().isBadRequest());
    }
}
