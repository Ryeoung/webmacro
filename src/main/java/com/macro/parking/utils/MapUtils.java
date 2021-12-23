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

    /**
     * @param parkingInfos entity 리스트
     * @return List<TicketDto>
     *     entity list -> dto list 변경
     */
    public List<TicketDto> convertAllToDto(List<ParkingInfo> parkingInfos) {
        return parkingInfos.stream()
                .map(parkingInfo -> this.convertToDto(parkingInfo))
                .collect(Collectors.toList());
    }

    /**
     * @param parkingInfo entity
     * @return TicketDto
     *  entity -> dto 변경
     */
    public TicketDto convertToDto(ParkingInfo parkingInfo) {
        return modelMapper.map(parkingInfo, TicketDto.class);
    }
}
