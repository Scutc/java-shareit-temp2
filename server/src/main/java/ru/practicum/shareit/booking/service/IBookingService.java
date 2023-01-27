package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface IBookingService {
    BookingDtoResponse createBooking(Long userId, BookingDto bookingDto);

    BookingDtoResponse updateBookingStatus(Long userId, Long bookingId, Boolean newStatus);

    List<BookingDtoResponse> getBookingsByUser(Long userId, String state, Integer from, Integer size);

    BookingDtoResponse getBookingById(Long bookingId, Long userId);

    List<BookingDtoResponse> getBookingByOwner(Long ownerId, String state, Integer from, Integer size);
}
