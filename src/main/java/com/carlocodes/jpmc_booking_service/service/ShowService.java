package com.carlocodes.jpmc_booking_service.service;

import com.carlocodes.jpmc_booking_service.ShowMapper.ShowMapper;
import com.carlocodes.jpmc_booking_service.dto.ShowDto;
import com.carlocodes.jpmc_booking_service.entity.Show;
import com.carlocodes.jpmc_booking_service.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    @Value("${booking.max-rows}")
    private int maxRows;
    @Value("${booking.max-seats-per-row}")
    private int maxSeatsPerRow;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public ShowDto setup(int rows, int numberOfSeatsPerRow, int cancellationWindowInMinutes) throws Exception {
        if (rows > maxRows || numberOfSeatsPerRow > maxSeatsPerRow)
            throw new Exception("Show setup failed: Exceeded maximum rows or seats per row.");

        Show show = new Show();
        show.setRows(rows);
        show.setSeatsPerRow(numberOfSeatsPerRow);
        show.setCancellationWindowInMinutes(cancellationWindowInMinutes);

        return ShowMapper.INSTANCE.mapToDto(showRepository.save(show));
    }

    public Optional<Show> findById(long id) {
        return showRepository.findById(id);
    }
}
