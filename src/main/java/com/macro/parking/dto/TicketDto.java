package com.macro.parking.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.macro.parking.domain.ParkingInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * view로 전달할 티켓 DTO
 */
@Setter
@Getter
@ToString
public class TicketDto {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime date;
	private int parkingInfoId;
	private String parkingLotName;
	private String appTicketName;
	private String carNum;
	private String code;
	private String webTicketName;
	private boolean checkApp;

	/**
	 * @param dto 비교할 dto
	 * @return boolean
	 * 	매개변수로 온 Dto가 동일한지 확인한다.
	 */
	public boolean isEqual(TicketDto dto) {
		if(dto.getDate().isEqual(this.getDate()) && 
        		dto.getParkingLotName().equals(this.getParkingLotName()) && 
        		dto.getCarNum().equals(this.getCarNum()) &&
        		dto.getAppTicketName().equals(this.getAppTicketName())){
			return true;
			
		}
		
		return false;
	}
	
}
