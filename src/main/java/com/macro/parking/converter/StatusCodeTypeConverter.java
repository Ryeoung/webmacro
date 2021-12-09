package com.macro.parking.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.macro.parking.enums.StatusCodeType;

@Converter
public class StatusCodeTypeConverter implements AttributeConverter<StatusCodeType, Integer> {

	/**
	 * @param attribute  주차권 코드
	 * @return Integer
	 *
	 * 	데이터베이스에 넣을 주차권 상태를 정수로 변환하여 저장.
	 */
	@Override
	public Integer convertToDatabaseColumn(StatusCodeType attribute) {

		if(attribute == null ||attribute.isEqual(StatusCodeType.NOT_WORKING)) {
			return 0;
		} else if(attribute.isEqual(StatusCodeType.SELENIUM_ERROR)) {
			return 1;
		} else if(attribute.isEqual(StatusCodeType.NO_CAR_ERROR)){
			return 2;
		} else if(attribute.isEqual(StatusCodeType.TICKET_EXIST_ERROR)) {
			return 3;
		} else if(attribute.isEqual(StatusCodeType.SUCCESS)) {
			return 4;
		} else if(attribute.isEqual(StatusCodeType.CHECK_TICKET)) {
			return 5;
		}
		return 6;
		
	}

	/**
	 * @param dbData 주차권 상태 정보
	 * @return StatusCodeType
	 *
	 * 		데이터베이스에서 받아온 주차권 상태 정보를 Enum으로 변환
환 */
	@Override
	public StatusCodeType convertToEntityAttribute(Integer dbData) {
		if( dbData == null || dbData == 0) {
			return StatusCodeType.NOT_WORKING;
		}  else if(dbData == 1) {
			return StatusCodeType.SELENIUM_ERROR;
		} else if(dbData == 2) {
			return StatusCodeType.NO_CAR_ERROR;
		} else if(dbData == 3) {
			return StatusCodeType.TICKET_EXIST_ERROR;

		} else if(dbData == 4) {
			return StatusCodeType.SUCCESS;

		} else if(dbData == 5) {
			return StatusCodeType.CHECK_TICKET;
		}
		return StatusCodeType.CANCEL;

	}



}
