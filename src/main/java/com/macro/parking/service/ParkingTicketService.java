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

	/**
	 * @param parkingLots 주차장 리스트
	 * @return List<ParkingTicket> 주차권 리스트
	 * 주차장 리스트를 기반으로 주차권 데이터 찾기
	 */
	public List<ParkingTicket> findByParkingLots(List<ParkingLot> parkingLots) {
		List<ParkingTicket> parkingTickets = new ArrayList<ParkingTicket>();
		List<ParkingTicket> findedTickets = null;
		for(ParkingLot parkingLot :parkingLots) {
			findedTickets = this.findByParkingLot(parkingLot);
			parkingTickets.addAll(findedTickets);
		}
		
		return parkingTickets;
	}

	/**
	 * @param parkingLot 주차장
	 * @return List<ParkingTicket>
	 *  해당 주차장에 맞는 주차권 데이터 찾기
	 */
	public List<ParkingTicket> findByParkingLot(ParkingLot parkingLot) {
		return parkingticketDao.findByParkingLot(parkingLot);
	}

	/**
	 * @param appName 어플리케이션 주차권 이름
	 * @param parkingLot 주차장
	 * @return ParkingTicket
	 *  어플리케이션 주차권 이름과 주차장 정보로 주차권 데이터 찾기
	 */
	public ParkingTicket findByAppNameAndParkingLot(String appName, ParkingLot parkingLot) {
		return parkingticketDao.findByAppNameAndParkingLot(appName, parkingLot);
	}
}
