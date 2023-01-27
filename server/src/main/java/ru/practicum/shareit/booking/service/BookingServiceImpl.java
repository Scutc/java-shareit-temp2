package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.utility.PaginationConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IItemService itemService;
    private final IUserService userService;
    private final PaginationConverter paginationConverter;

    @Transactional
    @Override
    public BookingDtoResponse createBooking(Long userId, BookingDto bookingDto) {
        UserDto userDto = userService.getUserById(userId);
        ItemDtoResponse itemDto = itemService.getItemById(bookingDto.getItemId(), userId);
        if (!itemDto.getAvailable()) {
            throw new NotAvailableException(bookingDto.getItemId());
        }
        if (!checkBookingDates(bookingDto)) {
            throw new NotValidDateException();
        }
        if (itemDto.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Нельзя забронировать собственную вещь!");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(UserMapper.toUser(userDto));
        booking.setItem(ItemMapper.toItem(itemDto));
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDtoResponse(booking);
    }

    @Transactional
    @Override
    public BookingDtoResponse updateBookingStatus(Long userId, Long bookingId, Boolean newStatus) {
        Booking booking = bookingRepository.findBookingById(bookingId, userId);
        if (booking == null) {
            throw new NotAllowedToChangeException("Бронирование соответствующее условию запроса не найдено");
        }
        booking = bookingRepository.findBookingByIdOwner(bookingId, userId);
        if (booking == null) {
            throw new EntityNotFoundException("У пользователя " + userId +
                    " Отсутствуют права на изменение статуса бронирования " + bookingId);
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED) && newStatus) {
            throw new NotAllowedToChangeException("Бронирование нельзя подтвердить повторно!");
        }
        if (newStatus) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    private boolean checkBookingDates(BookingDto bookingDto) {
        return (bookingDto.getStart().isBefore(bookingDto.getEnd()) && bookingDto.getStart()
                                                                                 .isAfter(LocalDateTime.now()));
    }

    @Override
    public BookingDtoResponse getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findBookingById(bookingId, userId);
        if (booking == null) {
            throw new EntityNotFoundException("Брониронивание с ID " + bookingId + " не найдено");
        }
        return BookingMapper.toBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDtoResponse> getBookingsByUser(Long userId, String state, Integer from, Integer size) throws UserNotFoundException {
        userService.getUserById(userId);
        Pageable pageable = paginationConverter.convert(from, size, "start");
        if (!pageable.isUnpaged()) {
            List<Booking> bookings = bookingRepository.findByBookerId(userId, pageable).toList();
            return makeListOfBookingDtoResponse(bookings);
        }
        switch (state) {
            case "ALL":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingsByBooker(userId));
            case "FUTURE":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingsByBookerFuture(userId));
            case "CURRENT":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingsByBookerCurrent(userId));
            case "PAST":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingsByBookerPast(userId));
            case "WAITING":
            case "REJECTED":
                return makeListOfBookingDtoResponse(bookingRepository
                        .findBookingsByBookerWithState(userId, BookingStatus.valueOf(state)));
            default:
                throw new UnsupportedStatusException(state);
        }
    }

    private List<BookingDtoResponse> makeListOfBookingDtoResponse(List<Booking> bookings) {
        return bookings.stream()
                       .map(BookingMapper::toBookingDtoResponse)
                       .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoResponse> getBookingByOwner(Long ownerId, String state, Integer from, Integer size) throws UserNotFoundException {
        userService.getUserById(ownerId);
        Pageable pageable = paginationConverter.convert(from, size, "start");
        if (!pageable.isUnpaged()) {
            List<Booking> bookings = bookingRepository.findByOwnerId(ownerId, pageable).toList();
            return makeListOfBookingDtoResponse(bookings);
        }
        switch (state) {
            case "ALL":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingByOwner(ownerId));
            case "FUTURE":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingByOwnerFuture(ownerId));
            case "CURRENT":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingsByOwnerCurrent(ownerId));
            case "PAST":
                return makeListOfBookingDtoResponse(bookingRepository.findBookingsByOwnerPast(ownerId));
            case "WAITING":
            case "REJECTED":
                return makeListOfBookingDtoResponse(bookingRepository
                        .findBookingByOwnerWithState(ownerId, BookingStatus.valueOf(state)));
            default:
                throw new UnsupportedStatusException(state);
        }
    }
}
