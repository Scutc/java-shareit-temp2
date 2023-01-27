package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody RequestDto requestDto) {
        log.info("Поступил запрос на создание Запроса на бронирование вещи");
        return requestClient.addRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Поступил запроc на получение списка Запросов от пользователя ID = {}", requestorId);
        return requestClient.findRequestByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findRequestsWithPagination(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @PositiveOrZero @RequestParam(required = false) Integer from,
                                                             @Positive @RequestParam(required = false) Integer size) {
        log.info("Поступил запроc на получение списка Запросов других пользователей от пользователя {}", userId);
        return requestClient.findAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение Запроса на бронирование с ID = {} от пользователя {}",
                requestId, userId);
        return requestClient.getRequestById(requestId, userId);
    }
}
