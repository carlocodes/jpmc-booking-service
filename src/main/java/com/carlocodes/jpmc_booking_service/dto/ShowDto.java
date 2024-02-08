package com.carlocodes.jpmc_booking_service.dto;

public class ShowDto {
    private Long id;
    private Integer rows;
    private Integer seatsPerRow;
    private Integer cancellationWindowInMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(Integer seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public Integer getCancellationWindowInMinutes() {
        return cancellationWindowInMinutes;
    }

    public void setCancellationWindowInMinutes(Integer cancellationWindowInMinutes) {
        this.cancellationWindowInMinutes = cancellationWindowInMinutes;
    }

    @Override
    public String toString() {
        return "ShowDto{" +
                "id=" + id +
                ", rows=" + rows +
                ", seatsPerRow=" + seatsPerRow +
                ", cancellationWindowInMinutes=" + cancellationWindowInMinutes +
                '}';
    }
}
