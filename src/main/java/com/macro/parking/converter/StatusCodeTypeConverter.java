package com.macro.parking.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.macro.parking.enums.StatusCodeType;

@Converter
public class StatusCodeTypeConverter implements AttributeConverter<StatusCodeType, Integer> {

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
