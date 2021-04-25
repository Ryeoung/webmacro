package com.macro.parking.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingTicket;

public interface ParkingInfoDao extends JpaRepository<ParkingInfo, Long>{
	
	ParkingInfo findTopByOrderTimeGreaterThanEqual(LocalDateTime cur);
	
	ParkingInfo findByParkingInfoId(int parkingInfoId);
	
	
	List<ParkingInfo> findByOrderTimeGreaterThanEqual(LocalDateTime cur);

}