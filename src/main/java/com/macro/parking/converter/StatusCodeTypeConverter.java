package com.macro.parking.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.macro.parking.enums.StatusCodeType;

@Converter
public class StatusCodeTypeConverter implements AttributeConverter<StatusCodeType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(StatusCodeType attribute) {
//		SUCCESS("ok"),
//		SELENIUM_ERROR("fail01"),
//		NO_CAR_ERROR("fail02"),
//		TICKET_EXIST_ERROR("fail03");
//
		if(attribute.isEqual(StatusCodeType.SELENIUM_ERROR)) {
			return 1;
		} else if(attribute.isEqual(StatusCodeType.NO_CAR_ERROR)){
			return 2;
		} else if(attribute.isEqual(StatusCodeType.TICKET_EXIST_ERROR)) {
			return 3;
		} else if(attribute.isEqual(StatusCodeType.SUCCESS)) {
			return 4;
		}
		return 0;
	}

	@Override
	public StatusCodeType convertToEntityAttribute(Integer dbData) {
		if( dbData == 0) {
			return StatusCodeType.NOT_WORKING;
		}  else if(dbData == 1) {
			return StatusCodeType.SELENIUM_ERROR;
		} else if(dbData == 2) {
			return StatusCodeType.NO_CAR_ERROR;
		} else if(dbData == 3) {
			return StatusCodeType.TICKET_EXIST_ERROR;

		}
			
		return StatusCodeType.TICKET_EXIST_ERROR;
	}



}
