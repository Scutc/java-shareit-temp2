package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                BookingStatus.WAITING,
                null,
                null);
    }

    public static BookingDto toBookingDto(Booking booking) {
        return booking != null ? new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId(),
                booking.getItem().getId()) : null;
    }

    public static BookingDtoResponse toBookingDtoResponse(Booking booking) {
        return booking != null ? new BookingDtoResponse(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker(),
                booking.getItem()) : null;
    }
}