package com.carlocodes.jpmc_booking_service.service;

import com.carlocodes.jpmc_booking_service.mapper.BookingMapper;
import com.carlocodes.jpmc_booking_service.dto.BookingDto;
import com.carlocodes.jpmc_booking_service.dto.TicketDto;
import com.carlocodes.jpmc_booking_service.entity.Booking;
import com.carlocodes.jpmc_booking_service.entity.Show;
import com.carlocodes.jpmc_booking_service.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ShowService showService;

    public BookingService(BookingRepository bookingRepository,
                          ShowService showService) {
        this.bookingRepository = bookingRepository;
        this.showService = showService;
    }

    public List<BookingDto> view(long showId) throws Exception {
        Show show = showService.findById(showId)
                .orElseThrow(() -> new Exception("Show not found!"));
        List<Booking> bookings = bookingRepository.findByShow(show);

        return BookingMapper.INSTANCE.mapToDtos(bookings);
    }

    public List<String> availability(long showId) throws Exception {
        Show show = showService.findById(showId)
                .orElseThrow(() -> new Exception("Show not found!"));

        List<String> seats = generateSeats(show.getRows(), show.getSeatsPerRow());
        List<String> bookedSeats = bookingRepository.findBookedSeatsByShowId(show.getId());

        seats.removeAll(bookedSeats);

        return seats;
    }

    private List<String> generateSeats(int rows, int seatsPerRow) {
        List<String> seats = new ArrayList<>();
        for (int row = 1; row <= rows; row++) {
            for (int seat = 1; seat <= seatsPerRow; seat++) {
                seats.add(generateSeat(row, seat));
            }
        }

        return seats;
    }

    private String generateSeat(int row, int seat) {
        char rowChar = (char) ('A' + row - 1);
        return rowChar + String.valueOf(seat);
    }

    public TicketDto book(long showId, String phoneNumber, String seats) throws Exception {
        Show show = showService.findById(showId)
                .orElseThrow(() -> new Exception("Show not found!"));

        String[] seatArray = seats.split(",");
        List<String> seatList = new ArrayList<>(Arrays.asList(seatArray));
        List<String> availableSeats = availability(show.getId());
        List<Booking> bookings = new ArrayList<>();

        for (String seat : seatList) {
            if (!availableSeats.contains(seat))
                throw new Exception("Seat " + seat + " is not available for booking.");

            Booking booking = new Booking();
            booking.setShow(show);
            booking.setPhoneNumber(phoneNumber);
            booking.setBookedSeat(seat);

            bookings.add(booking);
        }

        bookingRepository.saveAll(bookings);

        return mapTickets(bookings);
    }

    private TicketDto mapTickets(List<Booking> bookings) {
        List<String> tickets = bookings.stream()
                .map(Booking::getTicketNumber)
                .collect(Collectors.toList());

        TicketDto ticketDto = new TicketDto();
        ticketDto.setTickets(tickets);

        return ticketDto;
    }

    public void cancel(String phoneNumber, String ticketNumber) throws Exception {
        Booking booking = bookingRepository.findByPhoneNumberAndTicketNumber(phoneNumber, ticketNumber)
                .orElseThrow(() -> new Exception("Booking not found!"));

        Show show = booking.getShow();
        LocalDateTime bookingTime = booking.getCreatedDateTime();
        LocalDateTime now = LocalDateTime.now();
        long minutesElapsed = ChronoUnit.MINUTES.between(bookingTime, now);

        if (minutesElapsed > show.getCancellationWindowInMinutes())
            throw new Exception("Cancellation window has expired!");

        bookingRepository.delete(booking);
    }
}
