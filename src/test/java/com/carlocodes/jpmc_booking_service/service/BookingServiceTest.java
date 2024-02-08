package com.carlocodes.jpmc_booking_service.service;

import com.carlocodes.jpmc_booking_service.dto.BookingDto;
import com.carlocodes.jpmc_booking_service.dto.TicketDto;
import com.carlocodes.jpmc_booking_service.entity.Booking;
import com.carlocodes.jpmc_booking_service.entity.Show;
import com.carlocodes.jpmc_booking_service.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ShowService showService;

    @Test
    public void viewTest() throws Exception {
        long showId = 1L;
        int rows = 26;
        int numberOfSeatsPerRow = 10;
        int cancellationWindowInMinutes = 2;
        String phoneNumber = "09123456789";
        String seat = "A1";

        Show show = createShow(showId, rows, numberOfSeatsPerRow, cancellationWindowInMinutes);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(createBooking(show, phoneNumber, seat));

        when(showService.findById(anyLong())).thenReturn(Optional.of(show));
        when(bookingRepository.findByShow(any())).thenReturn(bookings);

        List<BookingDto> bookingDtos = bookingService.view(showId);

        assertNotNull(bookingDtos);
        assertEquals(1, bookingDtos.size());
        assertEquals(bookings.get(0).getShow().getId(), bookingDtos.get(0).getShowId());
        assertEquals(bookings.get(0).getPhoneNumber(), bookingDtos.get(0).getPhoneNumber());
        assertEquals(bookings.get(0).getBookedSeat(), bookingDtos.get(0).getSeats());
        assertEquals(bookings.get(0).getTicketNumber(), bookingDtos.get(0).getTicketNumber());
        assertEquals(bookings.get(0).getCreatedDateTime(), bookingDtos.get(0).getCreatedDateTime());

        verify(showService, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).findByShow(any());
    }

    @Test
    public void viewShowNotFoundTest() {
        long showId = 999L;

        when(showService.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> bookingService.view(showId));

        String expectedMessage = "Show not found!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(showService, times(1)).findById(anyLong());
    }

    @Test
    public void availabilityTest() throws Exception {
        long showId = 1L;
        int rows = 1;
        int numberOfSeatsPerRow = 1;
        int cancellationWindowInMinutes = 2;

        Show show = createShow(showId, rows, numberOfSeatsPerRow, cancellationWindowInMinutes);

        when(showService.findById(anyLong())).thenReturn(Optional.of(show));
        when(bookingRepository.findBookedSeatsByShowId(anyLong())).thenReturn(Collections.emptyList());

        List<String> expectedAvailableSeats = new ArrayList<>();
        expectedAvailableSeats.add("A1");

        List<String> actualAvailableSeats = bookingService.availability(showId);

        assertNotNull(actualAvailableSeats);
        assertEquals(expectedAvailableSeats, actualAvailableSeats);

        verify(showService, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).findBookedSeatsByShowId(anyLong());
    }

    @Test
    public void availabilityShowNotFoundTest() {
        long showId = 999L;

        when(showService.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> bookingService.availability(showId));

        String expectedMessage = "Show not found!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(showService, times(1)).findById(anyLong());
    }

    @Test
    public void bookTest() throws Exception {
        long showId = 1L;
        int rows = 1;
        int numberOfSeatsPerRow = 2;
        int cancellationWindowInMinutes = 2;
        String phoneNumber = "09123456789";
        String seat = "A1";

        Show show = createShow(showId, rows, numberOfSeatsPerRow, cancellationWindowInMinutes);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(createBooking(show, phoneNumber, seat));

        when(showService.findById(anyLong())).thenReturn(Optional.of(show));
        when(bookingRepository.saveAll(any())).thenReturn(bookings);
        when(bookingRepository.findBookedSeatsByShowId(anyLong())).thenReturn(Collections.emptyList());

        TicketDto ticketDto = bookingService.book(showId, phoneNumber, seat);

        assertNotNull(ticketDto);
        assertEquals(1, ticketDto.getTickets().size());

        List<String> expectedAvailableSeats = new ArrayList<>();
        expectedAvailableSeats.add("A2");

        when(bookingRepository.findBookedSeatsByShowId(anyLong())).thenReturn(List.of(seat));

        List<String> actualAvailableSeats = bookingService.availability(showId);

        assertEquals(expectedAvailableSeats, actualAvailableSeats);

        verify(showService, times(3)).findById(anyLong());
        verify(bookingRepository, times(1)).saveAll(any());
        verify(bookingRepository, times(2)).findBookedSeatsByShowId(anyLong());
    }

    @Test
    public void bookShowNotFoundTest() {
        long showId = 999L;
        String phoneNumber = "09123456789";
        String seat = "A1";

        when(showService.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> bookingService.book(showId, phoneNumber, seat));

        String expectedMessage = "Show not found!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(showService, times(1)).findById(anyLong());
    }

    @Test
    public void cancelTest() throws Exception {
        long showId = 1L;
        int rows = 1;
        int numberOfSeatsPerRow = 2;
        int cancellationWindowInMinutes = 2;
        String phoneNumber = "09123456789";
        String seat = "A1";

        Show show = createShow(showId, rows, numberOfSeatsPerRow, cancellationWindowInMinutes);
        Booking booking = createBooking(show, phoneNumber, seat);

        when(bookingRepository.findByPhoneNumberAndTicketNumber(anyString(), anyString())).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(any());

        bookingService.cancel(phoneNumber, booking.getTicketNumber());

        verify(bookingRepository, times(1)).findByPhoneNumberAndTicketNumber(anyString(), anyString());
        verify(bookingRepository, times(1)).delete(any());
    }

    @Test
    public void cancelBookingNotFoundTest() {
        String phoneNumber = "09123456789";
        String ticketNumber = "NA";

        when(bookingRepository.findByPhoneNumberAndTicketNumber(anyString(), anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> bookingService.cancel(phoneNumber, ticketNumber));

        String expectedMessage = "Booking not found!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(bookingRepository, times(1)).findByPhoneNumberAndTicketNumber(anyString(), anyString());
    }

    @Test
    public void cancelCancellationWindowExpired() {
        long showId = 1L;
        int rows = 1;
        int numberOfSeatsPerRow = 2;
        int cancellationWindowInMinutes = 2;
        String phoneNumber = "09123456789";
        String seat = "A1";

        Show show = createShow(showId, rows, numberOfSeatsPerRow, cancellationWindowInMinutes);
        Booking booking = createBooking(show, phoneNumber, seat);
        booking.setCreatedDateTime(LocalDateTime.now().minusHours(cancellationWindowInMinutes));

        when(bookingRepository.findByPhoneNumberAndTicketNumber(anyString(), anyString())).thenReturn(Optional.of(booking));

        Exception exception = assertThrows(Exception.class, () -> bookingService.cancel(phoneNumber, booking.getTicketNumber()));

        String expectedMessage = "Cancellation window has expired!";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(bookingRepository, times(1)).findByPhoneNumberAndTicketNumber(anyString(), anyString());
    }

    private Show createShow(long showId, int rows, int numberOfSeatsPerRow, int cancellationWindowInMinutes) {
        Show show = new Show();
        show.setId(showId);
        show.setRows(rows);
        show.setSeatsPerRow(numberOfSeatsPerRow);
        show.setCancellationWindowInMinutes(cancellationWindowInMinutes);

        return show;
    }

    private Booking createBooking(Show show, String phoneNumber, String seat) {
        Booking booking = new Booking();
        booking.setShow(show);
        booking.setPhoneNumber(phoneNumber);
        booking.setBookedSeat(seat);
        booking.setTicketNumber(UUID.randomUUID().toString());
        booking.setCreatedDateTime(LocalDateTime.now());

        return booking;
    }
}
