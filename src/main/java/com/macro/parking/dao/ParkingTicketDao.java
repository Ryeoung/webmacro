package com.macro.parking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;

public interface ParkingTicketDao extends JpaRepository<ParkingTicket, Long>{
	ParkingTicket findByAppNameAndParkingLot_ParkingLotId(String appName, int parkingLotId);
	
	List<ParkingTicket> findByParkingLot(ParkingLot parkingLot);
	ParkingTicket findByAppNameAndParkingLot(String appName, ParkingLot parkingLot);
}
