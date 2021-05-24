package com.macro.parking.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.dao.ParkingTicketDao;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;

@Service
public class ParkingTicketService {

	@Autowired
	ParkingTicketDao parkingticketDao;
	
	public List<ParkingTicket> findByParkingLots(List<ParkingLot> parkingLots) {
		List<ParkingTicket> parkingTickets = new ArrayList<ParkingTicket>();
		List<ParkingTicket> findedTickets = null;
		for(ParkingLot parkingLot :parkingLots) {
			findedTickets = this.findByParkingLot(parkingLot);
			parkingTickets.addAll(findedTickets);
		}
		
		return parkingTickets;
	}
	
	public List<ParkingTicket> findByParkingLot(ParkingLot parkingLot) {
		return parkingticketDao.findByParkingLot(parkingLot);
	}
	
	public ParkingTicket findByAppNameAndParkingLot(String appName, ParkingLot parkingLot) {
		return parkingticketDao.findByAppNameAndParkingLot(appName, parkingLot);
	}
}
