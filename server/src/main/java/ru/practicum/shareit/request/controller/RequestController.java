package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.RequestDto;
import ru.practicum.shareit.request.RequestDtoForResponse;
import ru.practicum.shareit.request.service.IRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestController {
    private final IRequestService requestService;

    @PostMapping
    public RequestDtoForResponse createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestBody RequestDto requestDto) {
        log.info("Поступил запрос на создание Запроса на бронирование вещи");
        return requestService.addRequest(requestDto, userId);
    }

    @GetMapping
    public List<RequestDtoForResponse> findRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Поступил запроc на получение списка Запросов от пользователя ID = {}", requestorId);
        return requestService.findRequestByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<RequestDtoForResponse> findRequestsWithPagination(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                  @RequestParam(required = false) Integer from,
                                                                  @RequestParam(required = false) Integer size) {
        log.info("Поступил запроc на получение списка Запросов других пользователей от пользователя {}", userId);
        return requestService.findAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDtoForResponse getRequestById(@PathVariable Long requestId,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение Запроса на бронирование с ID = {} от пользователя {}",
                requestId, userId);
        return requestService.getRequestById(requestId, userId);
    }
}
