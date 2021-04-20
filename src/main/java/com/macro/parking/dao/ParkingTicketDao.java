package com.macro.parking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.macro.parking.domain.ParkingTicket;

public interface ParkingTicketDao extends JpaRepository<ParkingTicket, Long>{
	ParkingTicket findTopByOrderByParkingTicketIdDesc();
	
	@Query("SELECT * FROM parking_ticket pt WHERE order_date >= CURDATE()")
	List<ParkingTicket> findAllByToday();
}
