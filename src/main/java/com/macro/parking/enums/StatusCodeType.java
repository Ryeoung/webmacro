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
	
	public static String getCode(StatusCodeType attribute) {
		
		if (attribute.isEqual(StatusCodeType.NOT_WORKING)) {
			return StatusCodeType.NOT_WORKING.getCode();
			
		} else if(attribute.isEqual(StatusCodeType.SELENIUM_ERROR)) {
			return StatusCodeType.SELENIUM_ERROR.getCode();
			
		} else if(attribute.isEqual(StatusCodeType.NO_CAR_ERROR)){
			return  StatusCodeType.NO_CAR_ERROR.getCode();
			
		} else if(attribute.isEqual(StatusCodeType.TICKET_EXIST_ERROR)) {
			return  StatusCodeType.TICKET_EXIST_ERROR.getCode();
			
		} else if(attribute.isEqual(StatusCodeType.SUCCESS)) {
			return  StatusCodeType.SUCCESS.getCode();
			
		} else if(attribute.isEqual(StatusCodeType.CHECK_TICKET)) {
			return  StatusCodeType.CHECK_TICKET.getCode();
		}
		return StatusCodeType.CANCEL.getCode();
	}
	
	public boolean isEqual(StatusCodeType target) {
		if(this.code.equals(target.getCode())) {
			return true;
		}
		return false;
	}
}
