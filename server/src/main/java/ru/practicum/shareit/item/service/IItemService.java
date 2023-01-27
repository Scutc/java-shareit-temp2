package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.List;

public interface IItemService {
    ItemDtoResponse getItemById(Long itemId, Long ownerId);

    List<ItemDtoResponse> findAllItems(Long ownerId);

    ItemDto createItem(Long userId, ItemDto itemDto) throws UserNotFoundException;

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws CustomSecurityException;

    List<ItemDto> searchItem(String searchText);

    CommentDto addComment(CommentDto commentDto, Long userId, Long itemId);
}
