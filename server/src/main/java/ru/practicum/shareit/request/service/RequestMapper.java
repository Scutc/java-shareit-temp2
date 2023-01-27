package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.RequestDto;
import ru.practicum.shareit.request.RequestDtoForResponse;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestMapper {
    public static Request toRequest(RequestDto requestDto) {
        return new Request(0L, requestDto.getDescription(), null, null, LocalDateTime.now());
    }

    public static RequestDtoForResponse toRequestDtoForResponse(Request request) {
        RequestDtoForResponse requestForResponse = new RequestDtoForResponse(request.getId(), request.getDescription(),
                request.getCreated(), new ArrayList<>());
        if (request.getItems() != null) {
            request.getItems().forEach(requestForResponse::addItem);
        }
        return requestForResponse;
    }
}
