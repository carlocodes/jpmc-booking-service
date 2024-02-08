package com.carlocodes.jpmc_booking_service.dto;

import java.util.List;

public class TicketDto {
    private List<String> tickets;

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "TicketDto{" +
                "tickets=" + tickets +
                '}';
    }
}
