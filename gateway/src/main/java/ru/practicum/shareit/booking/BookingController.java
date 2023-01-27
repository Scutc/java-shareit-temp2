package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", required = false) Integer from,
                                              @Positive @RequestParam(name = "size", required = false) Integer size) {
        BookingState state = BookingState.from(stateParam)
                                         .orElseThrow(() -> new UnsupportedStatusException(stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                    @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                    @PositiveOrZero @RequestParam(name = "from", required = false) Integer from,
                                                    @Positive @RequestParam(name = "size", required = false) Integer size) {
        log.info("Get booking, ownerId={}", ownerId);
        BookingState state = BookingState.from(stateParam)
                                         .orElseThrow(() -> new UnsupportedStatusException(stateParam));
        return bookingClient.getBookingByOwner(ownerId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.createBooking(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam(required = false) Boolean approved) {
        log.info("Поступил запрос на обновление бронирования ID = " + bookingId + " от пользователя" + userId);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }
}
