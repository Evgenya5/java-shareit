package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b from bookings as b" +
            "join items as it on it.id = b.item_id " +
            "join users as us on us.id = it.owner_id " +
            "where us.id = ?1", nativeQuery = true)
    List<Booking> findByOwner_IdOrderByStartDesc(Long ownerId);

    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);

    List<Booking> findByBooker_IdAndItem_IdAndEndIsBefore(Long bookerId, Long itemId, Instant end);
}
