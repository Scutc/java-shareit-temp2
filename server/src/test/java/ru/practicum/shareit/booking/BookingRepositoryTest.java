package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Test
    void findBookingByOwnerWithState() {
        EntityManager entityManager = em.getEntityManager();

        User user = new User(1L, "User1", "user1@email.com");
        user = userRepository.save(user);
        Item item = new Item(1L, "Item1", "drill for construction", true, user.getId(), null);
        item = itemRepository.save(item);

        Booking booking = new Booking(0L, LocalDateTime.now(), LocalDateTime.now().plusHours(2), BookingStatus.REJECTED,
                user, item);
        bookingRepository.save(booking);

        TypedQuery<Booking> query = em.getEntityManager()
                                      .createQuery("Select b from Booking b where b.status= :status", Booking.class);
        List<Booking> booking1 = Collections.singletonList(query.setParameter("status", BookingStatus.REJECTED)
                                                                .getSingleResult());

        assertTrue(bookingRepository.findBookingByOwnerWithState(user.getId(), BookingStatus.REJECTED).size() ==
                booking1.size());
    }
}
