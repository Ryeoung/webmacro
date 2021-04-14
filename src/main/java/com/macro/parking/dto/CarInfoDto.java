package com.macro.parking.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CarInfoDto {
	private LocalDateTime date;
	private String parkingLotName;
	private String ticket;
	private String carNum;
	private String code;
	private boolean checkApp;
}
