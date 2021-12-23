package com.macro.parking.enums;

/**
 * 요청의 결과를 표시하는 코드
 */
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


	/**
	 * @param target 비교할 대상
	 * @return boolean
	 *
	 *  비교할 대상과 동일한지 확인
	 */
	public boolean isEqual(StatusCodeType target) {
		if(this.code.equals(target.getCode())) {
			return true;
		}
		return false;
	}
}
