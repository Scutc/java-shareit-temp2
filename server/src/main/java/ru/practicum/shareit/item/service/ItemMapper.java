package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoResponseForRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                null);
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getUserId(), null);
    }

    public static ItemDtoResponse toItemDtoResponse(Item item, BookingDto lastBooking,
                                                    BookingDto nextBooking, List<CommentDto> comments) {
        return new ItemDtoResponse(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments,
                item.getOwnerId());
    }

    public static Item toItem(ItemDtoResponse itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getUserId(), null);
    }

    public static ItemDtoResponseForRequest toItemDtoResponseForRequest(Item item) {
        ItemDtoResponseForRequest itemForResponse = new ItemDtoResponseForRequest(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null);
        if (item.getRequest() != null) {
            itemForResponse.setRequestId(item.getRequest().getId());
        }
        return itemForResponse;
    }
}
