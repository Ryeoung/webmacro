package com.macro.parking.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.macro.parking.converter.StatusCodeTypeConverter;
import com.macro.parking.enums.StatusCodeType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "parking_info")
public class ParkingInfo {
	 @Id
	 @GeneratedValue
	 @Column(name = "parking_info_id", nullable = false)
	 private int parkingInfoId;
	 
	   @ManyToOne
	   @JoinColumn(name = "car_number", updatable = false)
	   private Car car;
	
	
	   @ManyToOne
	   @JoinColumn(name = "parking_ticket_id", updatable = false)
	   private ParkingTicket parkingTicket;
	
	   @Convert(converter = StatusCodeTypeConverter.class)
	   @Column(name = "app_flag")
	   private StatusCodeType appFlag;
	   

	   @Column(name = "order_time", columnDefinition = "TIMESTAMP")
	   private LocalDateTime orderTime;
	   
	   @Column(name = "parking_time", columnDefinition = "TIMESTAMP")
	   private LocalDateTime parkingTime;
	
	   @Column(name = "exit_time", columnDefinition = "TIMESTAMP")
	   private LocalDateTime exitTime;    
}
