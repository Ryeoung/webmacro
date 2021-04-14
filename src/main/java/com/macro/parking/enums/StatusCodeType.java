package com.macro.parking.enums;

public enum StatusCodeType {
	SUCCESS("ok"),
	LOGING_ERROR("fail01"),
	NO_CAR_ERROR("fail02"),
	TICKET_EXIST_ERROR("fail03");
	
	String code;

	StatusCodeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
