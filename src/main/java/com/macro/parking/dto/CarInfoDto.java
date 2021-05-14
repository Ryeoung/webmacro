package com.macro.parking.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingTicket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CarInfoDto {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime date;
	private int parkingInfoId;
	private String parkingLotName;
	private String appTicketName;
	private String carNum;
	private String code;
	private String webTicketName;
	private boolean checkApp;
	
	public boolean isEqual(CarInfoDto dto) {
		if(dto.getDate().isEqual(this.getDate()) && 
        		dto.getParkingLotName().equals(this.getParkingLotName()) && 
        		dto.getCarNum().equals(this.getCarNum()) &&
        		dto.getAppTicketName().equals(this.getAppTicketName())){
			return true;
			
		}
		
		return false;
	}
	public boolean isEqual(ParkingInfo parkingInfo) {
		
		CarInfoDto dto = new CarInfoDto();
		dto.setCarNum(parkingInfo.getCar().getNumber());
		dto.setWebTicketName(parkingInfo.getParkingTicket().getWebName());
		dto.setAppTicketName(parkingInfo.getParkingTicket().getAppName());
		dto.setParkingLotName(parkingInfo.getParkingTicket().getParkingLot().getName());
		dto.setDate(parkingInfo.getOrderTime());
		return isEqual(dto);
	}
}
