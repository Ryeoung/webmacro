package com.macro.parking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.macro.parking.dao.ParkingTicketDao;
import com.macro.parking.domain.ParkingTicket;

public class ParkingTicketService {
	@Autowired
	ParkingTicketDao parkingTicketDao;
	
	public ParkingTicket addTicket(ParkingTicket ticket) {
		return parkingTicketDao.save(ticket);
	}
	
	public List<ParkingTicket> addAllTicket(List<ParkingTicket> tickets) {
		return parkingTicketDao.save(tickets);
	}
}
