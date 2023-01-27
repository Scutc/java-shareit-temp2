package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void searchItemTest() {
        EntityManager entityManager = em.getEntityManager();
        User user = new User(1L, "User1", "user1@email.com");
        user = userRepository.save(user);
        Item item = new Item(1L, "Item1", "drill for construction", true, user.getId(), null);
        item = itemRepository.save(item);

        List<Item> itemForSearch = itemRepository.searchItem("const");

        assertTrue(itemForSearch.get(0).getName().equals(item.getName()));
    }
}
