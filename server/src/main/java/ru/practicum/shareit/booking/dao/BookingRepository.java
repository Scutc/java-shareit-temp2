package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.id =?1 AND (b.booker.id =?2 OR b.item.ownerId =?2)")
    Booking findBookingById(Long bookingId, Long userId);

    @Query("SELECT b FROM Booking b WHERE b.id =?1 AND b.item.ownerId =?2")
    Booking findBookingByIdOwner(Long bookingId, Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 ORDER BY b.id DESC")
    List<Booking> findBookingsByBooker(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.item.id = ?2 " +
            "AND b.status <> ru.practicum.shareit.booking.model.BookingStatus.REJECTED ORDER BY b.id DESC")
    List<Booking> findBookingsByItemAndBooker(Long userId, Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 AND b.status = ?2 ORDER BY b.id DESC")
    List<Booking> findBookingsByBookerWithState(Long userId, BookingStatus state);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 AND b.end > current_timestamp ORDER BY b.id DESC")
    List<Booking> findBookingsByBookerFuture(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 AND b.end > current_timestamp " +
            "AND b.start < current_timestamp ORDER BY b.id DESC")
    List<Booking> findBookingsByBookerCurrent(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 AND b.end > current_timestamp " +
            "AND b.start < current_timestamp ORDER BY b.id DESC")
    List<Booking> findBookingsByOwnerCurrent(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 AND b.end < current_timestamp " +
            "ORDER BY b.id DESC")
    List<Booking> findBookingsByBookerPast(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 AND b.end < current_timestamp " +
            "ORDER BY b.id DESC")
    List<Booking> findBookingsByOwnerPast(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 ORDER BY b.id DESC")
    List<Booking> findBookingByOwner(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 AND " +
            "b.status = ?2 ORDER BY b.id DESC")
    List<Booking> findBookingByOwnerWithState(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 AND b.end > current_timestamp ORDER BY b.id DESC")
    List<Booking> findBookingByOwnerFuture(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.item.ownerId = ?2 " +
            "AND b.status <> ru.practicum.shareit.booking.model.BookingStatus.REJECTED  ORDER BY b.start ASC")
    List<Booking> findBookingByItemAndOwner(Long itemId, Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId = ?1")
    Page<Booking> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1")
    Page<Booking> findByBookerId(Long bookerId, Pageable pageable);
}
