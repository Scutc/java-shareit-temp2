package ru.practicum.shareit.utility;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotValidParamsException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaginationConverterTests {

    PaginationConverter paginationConverter = new PaginationConverter();

    @Test
    void convertTest() {
        assertThrows(NotValidParamsException.class, () -> paginationConverter.convert(0, -1, null));

        assertNotNull(paginationConverter.convert(0, 2, null));

        assertNotNull(paginationConverter.convert(null, null, null));
    }
}
