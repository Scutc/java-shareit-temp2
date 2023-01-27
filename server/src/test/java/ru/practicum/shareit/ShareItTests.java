package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.request.RequestDto;
import ru.practicum.shareit.request.RequestDtoForResponse;
import ru.practicum.shareit.request.service.IRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ShareItTests {
    @Autowired
    IUserService userService;

    @Autowired
    IBookingService bookingService;

    @Autowired
    IItemService itemService;

    @Autowired
    IRequestService requestService;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void before() {

    }

    @Test
    void createGetUpdateDeleteUserTest() {
        UserDto userDto = new UserDto(0L, "User1", "user1@gmail.com");
        userDto = userService.createUser(userDto);
        UserDto userDto1 = userService.getUserById(userDto.getId());
        Long userId = userDto1.getId();

        assertThat(userDto.equals(userDto1));
        userDto1.setName("UserAfterUpdate");
        userService.updateUser(userId, userDto1);
        assertThat(userService.updateUser(userId, userDto1).getName().equals("UserAfterUpdate"));
        userService.deleteUser(userId);
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void createUserWithDuplicate() {
        UserDto userDto = new UserDto(0L, "User1", "user2@gmail.com");
        UserDto userDto1 = userService.createUser(userDto);
        assertThat(userDto1 == null);
        assertThat(userDto != null);
    }

    @Test
    void createBookingWithNoUser() {
        UserDto userDto = new UserDto(0L, "User1", "user3@gmail.com");
        userDto = userService.createUser(userDto);
        Long userId = userDto.getId();
        ItemDto itemDto = new ItemDto(0L, "Item1", "Description", true, userId, null);
        itemDto = itemService.createItem(userId, itemDto);
        Long itemId = itemDto.getId();

        BookingDto bookingDto = new BookingDto(0L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), BookingStatus.WAITING, 99L, itemId);
        assertThrows(UserNotFoundException.class, () -> bookingService.createBooking(99L, bookingDto));
    }

    @Test
    void createGetUpdateItemTest() {
        UserDto userDto = new UserDto(0L, "User1", "user4@gmail.com");
        userDto = userService.createUser(userDto);
        Long userId = userDto.getId();

        ItemDto itemDto = new ItemDto(0L, "Item1", "Description", true, userId, null);
        itemDto = itemService.createItem(userId, itemDto);
        Long itemId = itemDto.getId();
        assertThat(itemService.getItemById(itemId, userId).getName().equals("Item1"));

        itemDto.setName("ItemAfterUpdate");
        itemService.updateItem(userId, itemId, itemDto);
        assertThat(itemService.getItemById(itemId, userId).getName().equals("ItemAfterUpdate"));
    }

    @Test
    void bookingCreateGetUpdate() {
        UserDto userDto = new UserDto(0L, "User1", "user5@gmail.com");
        userDto = userService.createUser(userDto);
        Long userId = userDto.getId();
        userDto = new UserDto(0L, "User", "user6@gmail.com");
        userDto = userService.createUser(userDto);
        Long userId1 = userDto.getId();

        ItemDto itemDto = new ItemDto(0L, "Item1", "Description", true, userId, null);
        itemDto = itemService.createItem(userId, itemDto);
        Long itemId = itemDto.getId();

        BookingDto bookingDto = new BookingDto(0L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), BookingStatus.WAITING, userId1, itemId);
        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(userId1, bookingDto);
        Long bookingId = bookingDtoResponse.getId();

        assertThat(bookingService.getBookingById(bookingId, userId1) != null);

        bookingService.updateBookingStatus(userId, bookingId, true);
        assertThat(bookingService.getBookingById(bookingId, userId1).getStatus().equals(BookingStatus.APPROVED));
    }

    @Test
    void addCommentTest() throws InterruptedException {
        UserDto userDto = new UserDto(0L, "User7", "user7@gmail.com");
        userDto = userService.createUser(userDto);
        Long userId = userDto.getId();
        userDto = new UserDto(0L, "User8", "user8@gmail.com");
        userDto = userService.createUser(userDto);
        Long userId1 = userDto.getId();

        ItemDto itemDto = new ItemDto(0L, "Item1", "Description", true, userId, null);
        itemDto = itemService.createItem(userId, itemDto);
        Long itemId = itemDto.getId();

        BookingDto bookingDto = new BookingDto(0L, LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), BookingStatus.WAITING, userId1, itemId);
        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(userId1, bookingDto);
        Long bookingId = bookingDtoResponse.getId();
        bookingService.updateBookingStatus(userId, bookingId, true);

        Thread.sleep(3000);

        CommentDto commentDto = new CommentDto(0L, "Text", userDto.getName(), LocalDateTime.now());
        itemService.addComment(commentDto, userId1, itemDto.getId());

        assertThat(itemService.getItemById(itemDto.getId(), userId1).getComments().size() == 1);
    }

    @Test
    void createRequest() {
        UserDto userDto = new UserDto(0L, "User9", "user9@gmail.com");
        userDto = userService.createUser(userDto);
        RequestDto requestDto = new RequestDto(userDto.getId(), "Description");
        RequestDtoForResponse requestDtoForResponse = requestService.addRequest(requestDto, userDto.getId());
        assertThat(requestService.getRequestById(requestDtoForResponse.getId(), userDto.getId()) != null);
    }
}
