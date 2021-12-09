package com.macro.parking.utils;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.TicketDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapUtils {

    @Autowired
    ModelMapper modelMapper;

    public List<TicketDto> convertAllToDto(List<ParkingInfo> parkingInfos) {
        return parkingInfos.stream()
                .map(parkingInfo -> this.convertToDto(parkingInfo))
                .collect(Collectors.toList());
    }

    public TicketDto convertToDto(ParkingInfo parkingInfo) {
        return modelMapper.map(parkingInfo, TicketDto.class);
    }
}
