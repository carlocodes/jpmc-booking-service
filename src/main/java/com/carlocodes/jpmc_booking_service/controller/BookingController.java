package com.carlocodes.jpmc_booking_service.controller;

import com.carlocodes.jpmc_booking_service.dto.BookingDto;
import com.carlocodes.jpmc_booking_service.dto.TicketDto;
import com.carlocodes.jpmc_booking_service.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/admin/view/{showId}")
    public ResponseEntity<List<BookingDto>> view(@PathVariable long showId) throws Exception {
        return ResponseEntity.ok(bookingService.view(showId));
    }

    @GetMapping("/buyer/availability/{showId}")
    public ResponseEntity<List<String>> availability(@PathVariable long showId) throws Exception {
        return ResponseEntity.ok(bookingService.availability(showId));
    }

    @PostMapping("/buyer/book")
    public ResponseEntity<TicketDto> book(@RequestBody BookingDto bookingDto) throws Exception {
        return ResponseEntity.ok(bookingService.book(bookingDto.getShowId(), bookingDto.getPhoneNumber(), bookingDto.getSeats()));
    }

    @DeleteMapping("/buyer/cancel")
    public void cancel(@RequestBody BookingDto bookingDto) throws Exception {
        bookingService.cancel(bookingDto.getPhoneNumber(), bookingDto.getTicketNumber());
    }
}
