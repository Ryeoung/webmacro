package com.macro.parking.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.macro.parking.map.TicketDtoMapper;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper =  new ModelMapper();
		 modelMapper.addMappings(new TicketDtoMapper());

		//필드명이 같을 경우에만 적용
		modelMapper.getConfiguration().setImplicitMappingEnabled(false);
		return modelMapper;
	}	
}
