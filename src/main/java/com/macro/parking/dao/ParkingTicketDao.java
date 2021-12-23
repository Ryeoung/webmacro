package com.macro.parking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;

public interface ParkingTicketDao extends JpaRepository<ParkingTicket, Long>{

	/**
	 * @param parkingLot 주차장
	 * @return List<ParkingTicket>
	 *     해당 주차장에 예약한 주차권 정보를 가져온다.
	 */
	List<ParkingTicket> findByParkingLot(ParkingLot parkingLot);

	/**
	 * @param appName 어플리케이션에 저장된 주차권 이름
	 * @param parkingLot 주차장 이름
	 * @return ParkingTicket
	 *  해당 주차장에 appName과 같은 주차권 정보를 가져온다.
	 */
	ParkingTicket findByAppNameAndParkingLot(String appName, ParkingLot parkingLot);
}
