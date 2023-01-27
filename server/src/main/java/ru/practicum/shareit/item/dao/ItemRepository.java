package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemById(Long itemId);

    List<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE UPPER(i.name) like UPPER(CONCAT('%', ?1, '%')) " +
            " or UPPER(i.description) like UPPER(CONCAT('%', ?1, '%')) " +
            "and i.available = true")
    List<Item> searchItem(String tex);
}
