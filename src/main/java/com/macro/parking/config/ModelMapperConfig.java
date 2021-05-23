package com.macro.parking.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

import com.macro.parking.map.CarInfoDtoMapper;
import com.macro.parking.utils.MapUtils;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper =  new ModelMapper();
		 modelMapper.addMappings(new CarInfoDtoMapper());

		//필드명이 같을 경우에만 적용
		modelMapper.getConfiguration().setImplicitMappingEnabled(false);
		return modelMapper;
	}	
}
