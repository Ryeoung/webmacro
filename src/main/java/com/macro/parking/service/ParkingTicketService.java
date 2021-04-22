package com.macro.parking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
	public List<ParkingTicket> updateByAppFlag(List<ParkingTicket> tickets) {
		List<ParkingTicket> appCheckedTickets = new LinkedList<>();
		for(ParkingTicket ticket :tickets) {
			if(ticket.isAppFlag()) {
				appCheckedTickets.add(ticket);
			}
		}
		System.out.println("바뀐 사이즈 " + appCheckedTickets.size());
		return parkingTicketDao.save(appCheckedTickets);
	}
	
	public ParkingTicket findLastParkingTicket() {
		return parkingTicketDao.findTopByOrderTimeGreaterThanEqual(getToday());
	}
	
	public List<ParkingTicket> findAllByToday() {
		return parkingTicketDao.findByOrderTimeGreaterThanEqual(getToday());
	}
	public ParkingTicket findByParkingTicketId(int id) {
		return parkingTicketDao.findByParkingTicketId(id);
	}
	
	private LocalDateTime getToday() {
		LocalDate today = LocalDate.now();
		return today.atStartOfDay();
	}
}
