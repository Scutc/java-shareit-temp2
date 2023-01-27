package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    void findRequestByRequestorIdTest() {
        User user = new User(1L, "User1", "user1@email.com");
        user = userRepository.save(user);
        Item item = new Item(1L, "Item1", "drill for construction", true, user.getId(), null);
        item = itemRepository.save(item);
        Request request = new Request(0L, "request description", List.of(item), user, LocalDateTime.now());
        request = requestRepository.save(request);

        TypedQuery<Request> query = em.getEntityManager()
                                      .createQuery("Select r from Request r where r.requestor.id = :id", Request.class);
        Request request1 = query.setParameter("id", request.getRequestor().getId()).getSingleResult();
        assertTrue(requestRepository.findRequestByRequestor_Id(request.getRequestor().getId()).get(0).getId()
                                    .equals(request1.getId()));
    }
}
