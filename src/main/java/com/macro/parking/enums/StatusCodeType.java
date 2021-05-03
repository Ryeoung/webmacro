package com.macro.parking.enums;

public enum StatusCodeType {
	SUCCESS("ok"),
	CHECK_TICKET("check"),
	NOT_WORKING("noting"),
	CANCEL("cancel"),
	SELENIUM_ERROR("fail01"),
	NO_CAR_ERROR("fail02"),
	TICKET_EXIST_ERROR("fail03");
	
	String code;

	StatusCodeType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public boolean isEqual(StatusCodeType target) {
		if(this.code.equals(target.getCode())) {
			return true;
		}
		return false;
	}
}
