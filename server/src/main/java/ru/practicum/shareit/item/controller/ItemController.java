package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.IItemService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final IItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDtoResponse getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Поступил запрос на получение товара с ID {} от пользователя {}", itemId, ownerId);
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDtoResponse> findAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение всех товаров");
        return itemService.findAllItems(userId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на создание товара");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление товара с ID {}", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск товаров по тексту \"{}\"", text);
        if (text.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        return itemService.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Получен запрос на добавление комментария для вещи {} от пользователя {}", itemId, userId);
        return itemService.addComment(commentDto, userId, itemId);
    }
}
