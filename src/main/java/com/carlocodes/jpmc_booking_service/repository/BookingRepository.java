package com.carlocodes.jpmc_booking_service.repository;

import com.carlocodes.jpmc_booking_service.entity.Booking;
import com.carlocodes.jpmc_booking_service.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByShow(Show show);

    @Query(value = "SELECT b.booked_seat FROM booking b WHERE b.show_id = :showId", nativeQuery = true)
    List<String> findBookedSeatsByShowId(@Param("showId") Long showId);

    Optional<Booking> findByPhoneNumberAndTicketNumber(String phoneNumber, String ticketNumber);
}
