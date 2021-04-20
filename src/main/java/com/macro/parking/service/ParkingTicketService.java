package com.macro.parking.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.dao.ParkingTicketDao;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.CarInfoDto;

@Service
public class ParkingTicketService {
	@Autowired
	ParkingTicketDao parkingTicketDao;
	
	public ParkingTicket addTicket(ParkingTicket ticket) {
		return parkingTicketDao.save(ticket);
	}
	
	public List<ParkingTicket> addAllTicket(List<ParkingTicket> tickets) {
		return parkingTicketDao.save(tickets);
	}
	
	public ParkingTicket findLastParkingTicket() {
		return parkingTicketDao.findTopByOrderByParkingTicketIdDesc();
	}
	
	public List<ParkingTicket> findAllByToday() {
		return parkingTicketDao.findAllByToday();
	}
}
