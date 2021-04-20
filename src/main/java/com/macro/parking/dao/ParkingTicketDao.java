package com.macro.parking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.ParkingTicket;

public interface ParkingTicketDao extends JpaRepository<ParkingTicket, Long>{
	ParkingTicket findTopByOrderByParkingTicketIdDesc();
	
}
