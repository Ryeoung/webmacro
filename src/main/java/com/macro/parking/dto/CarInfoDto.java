package com.macro.parking.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.macro.parking.domain.ParkingTicket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CarInfoDto {
	private LocalDateTime date;
	private int parkingTicketId;
	private String parkingLotName;
	private String ticket;
	private String carNum;
	private String code;
	private boolean checkApp;
	
	public boolean isEqual(CarInfoDto dto) {
		System.out.println(dto);
		if(dto.getDate().isEqual(this.getDate()) && 
        		dto.getParkingLotName().equals(this.getParkingLotName()) && 
        		dto.getCarNum().equals(this.getCarNum()) &&
        		dto.getTicket().equals(this.getTicket())){
			return true;
			
		}
		
		return false;
	}
	public boolean isEqual(ParkingTicket ticket) {
		
		CarInfoDto dto = new CarInfoDto();
		dto.setCarNum(ticket.getCar().getNumber());
		dto.setTicket(ticket.getParkingTicketName());
		dto.setParkingLotName(ticket.getParkingLot().getName());
		dto.setDate(ticket.getOrderTime());
		return isEqual(dto);
	}
}
