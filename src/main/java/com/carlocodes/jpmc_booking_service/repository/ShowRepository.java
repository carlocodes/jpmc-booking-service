package com.carlocodes.jpmc_booking_service.repository;

import com.carlocodes.jpmc_booking_service.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
}
