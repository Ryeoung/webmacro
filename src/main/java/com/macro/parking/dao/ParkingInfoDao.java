package com.macro.parking.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.enums.StatusCodeType;

public interface ParkingInfoDao extends JpaRepository<ParkingInfo, Long>{
	
	ParkingInfo findTopByOrderTimeGreaterThanEqual(LocalDateTime cur);
	ParkingInfo findTopByOrderTimeGreaterThanEqualOrderByOrderTimeDescParkingInfoIdDesc(LocalDateTime cur);
	ParkingInfo findByParkingInfoId(int parkingInfoId);
	
	List<ParkingInfo> findByAppFlagInAndOrderTimeGreaterThanEqual(List<StatusCodeType> appFlags, LocalDateTime cur);
	List<ParkingInfo> findByAppFlagInAndParkingTicketInAndOrderTimeGreaterThanEqual(List<StatusCodeType> appFlags,List<ParkingTicket> parkingTickets, LocalDateTime cur);
	List<ParkingInfo> findByOrderTimeGreaterThanEqual(LocalDateTime cur);
	List<ParkingInfo> findByOrderTimeGreaterThanEqualAndParkingTicketInOrCar_NumberLike(LocalDateTime cur, List<ParkingTicket> parkingTickets, String carNumber);

}