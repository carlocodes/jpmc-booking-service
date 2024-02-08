package com.carlocodes.jpmc_booking_service.ShowMapper;

import com.carlocodes.jpmc_booking_service.dto.BookingDto;
import com.carlocodes.jpmc_booking_service.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "show.id", target = "showId")
    @Mapping(source = "bookedSeat", target = "seats")
    BookingDto mapToDto(Booking booking);

    List<BookingDto> mapToDtos(List<Booking> bookings);
}
