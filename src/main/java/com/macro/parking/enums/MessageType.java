package com.macro.parking.enums;

public enum MessageType {
	SUCCESS("ok","주차권 발"),
	LOGING_ERROR("fail01", "로그인 실패"),
	NO_CAR_ERROR("fail02", "차량 없음 "),
	TICKET_EXIST_ERROR("fail03", "이미 주차권이 발급된 상태");
	
	String code;
	String message;

	MessageType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
