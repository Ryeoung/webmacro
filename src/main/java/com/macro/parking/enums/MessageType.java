package com.macro.parking.enums;

public enum MessageType {
	SUCCESS("ok","티켓구입 성공"),
	LOGING_ERROR("fail01", "로그인 실패"),
	NO_CAR_ERROR("fail02", "현재 차가 없음"),
	TICKET_EXIST_ERROR("fail03", "티켓 이미 존");
	
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
