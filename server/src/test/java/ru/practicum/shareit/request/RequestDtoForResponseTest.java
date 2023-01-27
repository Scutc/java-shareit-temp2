package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


public class RequestDtoForResponseTest {
    RequestDtoForResponse requestDtoForResponse;

    @Test
    void testAddItem() {
        requestDtoForResponse = new RequestDtoForResponse(1L, "description",
                LocalDateTime.now(), new ArrayList<>());
        assertThat(requestDtoForResponse.getItems().size() == 0);

        Item item = new Item(1L, "name", "description", true, 1L, null);
        requestDtoForResponse.addItem(item);
        assertThat(requestDtoForResponse.getItems().size() == 1);
    }
}
