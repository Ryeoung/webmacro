package com.macro.parking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.ParkingTicket;

public interface ParkingTicketDao extends JpaRepository<ParkingTicket, Long>{
	ParkingTicket findByAppNameAndPakringLotId(String appName, int parkingLotId);

}
