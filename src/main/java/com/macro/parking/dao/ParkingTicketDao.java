package com.macro.parking.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.macro.parking.domain.ParkingTicket;

public interface ParkingTicketDao extends JpaRepository<ParkingTicket, Long>{
	
	ParkingTicket findTopByOrderTimeGreaterThanEqual(LocalDateTime cur);
	
	ParkingTicket findByParkingTicketId(int parkingTicketId);
	
	
	List<ParkingTicket> findByOrderTimeGreaterThanEqual(LocalDateTime cur);
}