package ru.practicum.shareit.utility;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotValidParamsException;

@Component
@Data
public class PaginationConverter {

    public Pageable convert(Integer from, Integer size, String sortBy) {
        Pageable pageable;
        if (from == null && size == null) {
            pageable = Pageable.unpaged();
        } else {
            pageable = sortBy != null ? PageRequest.of(from, 1, Sort.by(sortBy).descending())
                    : PageRequest.of(from, 1);
        }
        return pageable;
    }
}
