package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.utility.PaginationConverter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingUnitTests {

    IBookingService bookingService;
    BookingDto bookingDto;
    Booking booking;
    BookingDtoResponse bookingDtoResponse;
    ItemDto itemDto;
    ItemDtoResponse itemDtoResponse;
    User user;
    UserDto userDto;
    Item item;

    @Mock
    IItemService itemService;

    @Mock
    IUserService userService;

    @Mock
    BookingRepository bookingRepository;


    @BeforeEach
    void setUp() {
        user = new User(1L, "User1", "user1@mail.com");
        userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        itemDto = new ItemDto(1L, "Item1", "Description1", true, 2L, null);
        item = new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                itemDto.getUserId(), null);
        bookingService = new BookingServiceImpl(bookingRepository, itemService, userService, new PaginationConverter());
        bookingDto = new BookingDto(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(5),
                BookingStatus.WAITING, 1L, 1L);
        booking = new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                bookingDto.getBookingStatus(), null, null);
        bookingDtoResponse = new BookingDtoResponse(booking.getId(), booking.getStart(), booking.getEnd(),
                booking.getStatus(), null, null);

        itemDtoResponse = new ItemDtoResponse(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), null, null, null, 2L);
    }

    @Test
    void createBookingTest() {
        Mockito
                .when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDtoResponse);

        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);

        assertThat(bookingService.createBooking(1L, bookingDto) != null);

        itemDtoResponse.setUserId(1L);
        assertThrows(EntityNotFoundException.class, () -> bookingService.createBooking(1L, bookingDto));

        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        assertThrows(NotValidDateException.class, () -> bookingService.createBooking(1L, bookingDto));

        itemDtoResponse.setAvailable(false);
        Mockito
                .when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDtoResponse);
        assertThrows(NotAvailableException.class, () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    void updateBookingStatusTest() {
        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(null);

        assertThrows(NotAllowedToChangeException.class, () -> bookingService.updateBookingStatus(1L, 1L,
                true));

        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(booking);
        Mockito
                .when(bookingRepository.findBookingByIdOwner(any(), any()))
                .thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> bookingService.updateBookingStatus(1L, 1L,
                true));


        booking.setStatus(BookingStatus.APPROVED);
        Mockito
                .when(bookingRepository.findBookingByIdOwner(any(), any()))
                .thenReturn(booking);

        assertThrows(NotAllowedToChangeException.class, () -> bookingService.updateBookingStatus(1L, 1L,
                true));


        booking.setStatus(BookingStatus.WAITING);
        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(booking);
        Mockito
                .when(bookingRepository.findBookingByIdOwner(any(), any()))
                .thenReturn(booking);
        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);


        assertThat(bookingService.updateBookingStatus(1L, 1L, true)
                                 .getStatus().equals(BookingStatus.APPROVED));

        assertThat(bookingService.updateBookingStatus(1L, 1L, false)
                                 .getStatus().equals(BookingStatus.REJECTED));
    }

    @Test
    void getBookingByIdTest() {
        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(booking);

        assertNotNull(bookingService.getBookingById(1L, 1L));

        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBookingsByUserTest() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);

        assertThrows(UnsupportedStatusException.class, () ->
                bookingService.getBookingsByUser(1L, "Unsupported", null, null));

        Mockito
                .when(bookingRepository.findBookingsByBooker(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);
        Mockito
                .when(bookingRepository.findBookingsByBooker(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByBookerFuture(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByBookerCurrent(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByBookerPast(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByBookerWithState(any(), any()))
                .thenReturn(List.of(booking));
        assertThat(bookingService.getBookingsByUser(1L, "ALL", null, null).size() == 1);
        assertThat(bookingService.getBookingsByUser(1L, "FUTURE", null, null).size() == 1);
        assertThat(bookingService.getBookingsByUser(1L, "CURRENT", null, null).size() == 1);
        assertThat(bookingService.getBookingsByUser(1L, "PAST", null, null).size() == 1);
        assertThat(bookingService.getBookingsByUser(1L, "WAITING", null, null).size() == 1);
        assertThat(bookingService.getBookingsByUser(1L, "REJECTED", null, null).size() == 1);
    }

    @Test
    void getBookingsByOwnerTest() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);

        assertThrows(UnsupportedStatusException.class, () ->
                bookingService.getBookingByOwner(1L, "Unsupported", null, null));

        Mockito
                .when(bookingRepository.findBookingByOwner(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingByOwnerFuture(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByOwnerCurrent(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByOwnerPast(any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingByOwnerWithState(any(), any()))
                .thenReturn(List.of(booking));
        assertThat(bookingService.getBookingByOwner(1L, "ALL", null, null).size() == 1);
        assertThat(bookingService.getBookingByOwner(1L, "FUTURE", null, null).size() == 1);
        assertThat(bookingService.getBookingByOwner(1L, "CURRENT", null, null).size() == 1);
        assertThat(bookingService.getBookingByOwner(1L, "PAST", null, null).size() == 1);
        assertThat(bookingService.getBookingByOwner(1L, "WAITING", null, null).size() == 1);
        assertThat(bookingService.getBookingByOwner(1L, "REJECTED", null, null).size() == 1);
    }
}
