package com.carlocodes.jpmc_booking_service.mapper;

import com.carlocodes.jpmc_booking_service.dto.ShowDto;
import com.carlocodes.jpmc_booking_service.entity.Show;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShowMapper {
    ShowMapper INSTANCE = Mappers.getMapper(ShowMapper.class);

    ShowDto mapToDto(Show show);
}
