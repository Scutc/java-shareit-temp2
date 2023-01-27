package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.IRequestService;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.utility.PaginationConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RequestUnitTests {
    IRequestService requestService;
    Item item;
    ItemDto itemDto;
    User user;
    UserDto userDto;
    Request request;
    RequestDto requestDto;
    RequestDtoForResponse requestDtoForResponse;
    Pageable pageable;

    @Mock
    RequestRepository requestRepository;

    @Mock
    IUserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PaginationConverter paginationConverter;

    @BeforeEach
    void setUp() {
        requestService = new RequestServiceImpl(requestRepository, userService,
                userRepository, paginationConverter);
        item = new Item(1L, "Item1", "description", true, 1L, null);
        itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getId(), 1L);
        user = new User(1L, "User1", "user1@mail.com");
        userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        request = new Request(1L, "desc", List.of(item), user, LocalDateTime.now());
        requestDto = new RequestDto(1L, "desc");
        requestDtoForResponse = new RequestDtoForResponse(request.getId(), request.getDescription(),
                request.getCreated(), new ArrayList<>());
        pageable = PageRequest.of(0, 1);

    }

    @Test
    void addRequestTest() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        Mockito
                .when(requestRepository.save(any()))
                .thenReturn(request);

        assertThat(requestService.addRequest(requestDto, 1L) != null);
    }

    @Test
    void findRequestByRequestorIdTest() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);
        Mockito
                .when(requestRepository.findRequestByRequestor_Id(any()))
                .thenReturn(List.of(request));

        assertThat(requestService.findRequestByRequestorId(1L).size() == 1);
    }

    @Test
    void testFindAllRequests() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);
        Mockito
                .when(paginationConverter.convert(any(),any(),any()))
                .thenReturn(pageable);
        Mockito
                .when(requestRepository.findAllRequests(any(),any()))
                .thenReturn(List.of(request));

        assertThat(requestService.findAllRequests(1L, 0, 1).size() == 1);
    }

    @Test
    void getRequestById() {
        Mockito
                .when(userService.getUserById(any()))
                .thenReturn(userDto);
        Mockito
                .when(requestRepository.findRequestById(any()))
                .thenReturn(null);

        assertThrows(RequestNotFoundException.class,
                () -> requestService.getRequestById(1L, 1L));

        Mockito
                .when(requestRepository.findRequestById(any()))
                .thenReturn(request);

        assertThat(requestService.getRequestById(1L, 1L) != null);
    }
}
