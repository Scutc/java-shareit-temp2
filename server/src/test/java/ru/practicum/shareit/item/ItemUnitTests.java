package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemUnitTests {

    IItemService itemService;
    Item item;
    ItemDto itemDto;
    Booking booking;
    BookingDto bookingDto;
    User user;
    UserDto userDto;
    CommentDto commentDto;
    Comment comment;

    @Mock
    ItemRepository itemRepository;

    @Mock
    IUserService userService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    RequestRepository requestRepository;


    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userService, bookingRepository,
                commentRepository, requestRepository);

        item = new Item(1L, "Item1", "description", true, 1L, null);
        itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getId(), 1L);
        user = new User(1L, "User1", "user1@mail.com");
        userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        bookingDto = bookingDto = new BookingDto(1L, LocalDateTime.now().minusHours(1), LocalDateTime.now()
                                                                                                     .minusHours(5),
                BookingStatus.WAITING, 1L, 1L);
        booking = new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                bookingDto.getBookingStatus(), user, item);
        commentDto = new CommentDto(1L, "comment", "name", LocalDateTime.now());
        comment = new Comment(1L, "comment", item, user, LocalDateTime.now());
    }

    @Test
    void getItemByIdTest() {
        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(null);
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L, 1L));

        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);
        Mockito
                .when(bookingRepository.findBookingByItemAndOwner(1L, 1L))
                .thenReturn(List.of(booking));
        Mockito
                .when(commentRepository.findCommentsById(1L))
                .thenReturn(new ArrayList<>());

        assertThat(itemService.getItemById(1L, 1L).getId().equals(1L));
    }

    @Test
    void findAllItemsTest() {
        Mockito
                .when(itemRepository.findAllByOwnerIdOrderByIdAsc(any()))
                .thenReturn(List.of(item));
        Mockito
                .when(bookingRepository.findBookingByItemAndOwner(any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(commentRepository.findCommentsById(any()))
                .thenReturn(new ArrayList<>());

        assertThat(itemService.findAllItems(1L).size() == 1);
    }

    @Test
    void createItemTest() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);
        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);
        Mockito
                .when(requestRepository.findRequestById(any()))
                .thenReturn(null);

        assertThat(itemService.createItem(1L, itemDto).getId().equals(1L));
    }

    @Test
    void updateItemTest() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);

        item.setOwnerId(2L);
        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);
        assertThrows(CustomSecurityException.class, () -> itemService.updateItem(1L, 2L, itemDto));

        item.setOwnerId(1L);
        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);
        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);

        assertThat(itemService.updateItem(1L, 1L, itemDto).getId().equals(1L));
    }

    @Test
    void searchItemTest() {
        Mockito
                .when(itemRepository.searchItem(any()))
                .thenReturn(List.of(item));

        assertThat(itemService.searchItem("text").size() == 1);
    }

    @Test
    void addCommentTest() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);
        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(comment);
        Mockito
                .when(bookingRepository.findBookingsByItemAndBooker(any(), any()))
                .thenReturn(List.of(booking));

        assertThat(itemService.addComment(commentDto, 1L, 1L) != null);
    }
}
