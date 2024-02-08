package com.carlocodes.jpmc_booking_service.dto;

import java.time.LocalDateTime;

public class BookingDto {
    private Long id;
    private Long showId;
    private String phoneNumber;
    private String seats;
    private String ticketNumber;
    private LocalDateTime createdDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        return "BookingDto{" +
                "id=" + id +
                ", showId=" + showId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", seats='" + seats + '\'' +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
