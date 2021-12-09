package com.macro.parking.map;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.TicketDto;
import com.macro.parking.enums.StatusCodeType;

@Component
public class CarInfoDtoMapper  extends PropertyMap<ParkingInfo, TicketDto>{

	@Override
	protected void configure() {
		this.map().setCarNum(this.source.getCar().getNumber());
		this.map().setAppTicketName(this.source.getParkingTicket().getAppName());
		this.map().setDate(this.source.getOrderTime());
		using(enumConvertor()).map(this.source.getAppFlag()).setCode(null);
		String parkingLotName = this.source.getParkingTicket().getParkingLot().getName();
		this.map().setParkingLotName(parkingLotName);
	}
	
	private Converter<StatusCodeType, String> enumConvertor() {
		Converter<StatusCodeType, String> enumConverter = 
				ctx -> ctx.getSource() == null ? null : ctx.getSource().getCode();	
	    return  enumConverter;
	}
		
}
