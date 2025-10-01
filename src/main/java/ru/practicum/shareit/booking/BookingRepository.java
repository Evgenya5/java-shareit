package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b.* from bookings as b " +
            "join items as it on it.id = b.item_id " +
            "and it.owner_id = ?1 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findByOwner_IdOrderByStartDesc(Long ownerId);

    @Query(value = "select b.* from bookings as b " +
            "join items as it on it.id = b.item_id " +
            "and it.owner_id = ?1 " +
            "where b.end_date >= ?2 " +
            "and b.start_date <= ?3 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findByOwner_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime end, LocalDateTime start);

    @Query(value = "select b.* from bookings as b " +
            "join items as it on it.id = b.item_id " +
            "and it.owner_id = ?1 " +
            "where b.end_date <= ?2 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findByOwner_IdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime end);

    @Query(value = "select b.* from bookings as b " +
            "join items as it on it.id = b.item_id " +
            "and it.owner_id = ?1 " +
            "where b.start_date >= ?2 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findByOwner_IdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime start);
    
    @Query(value = "select b.* from bookings as b " +
            "join items as it on it.id = b.item_id " +
            "and it.owner_id = ?1 " +
            "where b.status = ?2 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findByOwner_IdAndStatusOrderByStartDesc(Long bookerId, String status);

    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);

    List<Booking> findByBooker_IdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, LocalDateTime start);

    List<Booking> findByBooker_IdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBooker_IdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByBooker_IdAndItem_IdAndEndIsBefore(Long bookerId, Long itemId, LocalDateTime end);

    List<Booking> findByItem_IdAndEndIsAfterAndStartIsBefore(Long itemId, LocalDateTime end, LocalDateTime start);

}
