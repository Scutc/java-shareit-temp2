package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;


import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/items")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Поступил запрос на получение товара с ID {} от пользователя {}", itemId, ownerId);
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение всех товаров");
        return itemClient.findAllItems(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Поступил запрос на создание товара");
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление товара с ID {}", itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск товаров по тексту \"{}\"", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
        return itemClient.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос на добавление комментария для вещи {} от пользователя {}", itemId, userId);
        return itemClient.addComment(commentDto, userId, itemId);
    }
}
