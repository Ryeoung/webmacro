package com.macro.parking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.macro.parking.dao.ParkingInfoDao;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.enums.StatusCodeType;

@Service
public class ParkingInfoService {
	@Autowired
	ParkingInfoDao parkingInfoDao;
	
	public ParkingInfo addTicket(ParkingInfo parkingInfo) {
		return parkingInfoDao.save(parkingInfo);
	}
	
	public List<ParkingInfo> addAllTicket(List<ParkingInfo> parkingInfos) {
		return parkingInfoDao.save(parkingInfos);
	}
	public List<ParkingInfo> updateByAppFlag(List<ParkingInfo> parkingInfos) {
		List<ParkingInfo> appCheckedTickets = new LinkedList<>();
		for(ParkingInfo info :parkingInfos) {
				appCheckedTickets.add(info);
		}
		System.out.println("바뀐 사이즈 " + appCheckedTickets.size());
		return parkingInfoDao.save(appCheckedTickets);
	}
	public List<ParkingInfo> findAllWillCrawling() {
		List<StatusCodeType> appFlags = new ArrayList<>();
		appFlags.add(StatusCodeType.NO_CAR_ERROR);
		appFlags.add(StatusCodeType.NOT_WORKING);
		appFlags.add(StatusCodeType.SELENIUM_ERROR);
		
		return parkingInfoDao.findByAppFlagInAndOrderTimeGreaterThanEqual(appFlags, getToday());
	}
	public ParkingInfo findLastParkingTicket() {
		return parkingInfoDao.findTopByOrderTimeGreaterThanEqual(getToday());
	}
	
	public List<ParkingInfo> findAllByToday() {
		return parkingInfoDao.findByOrderTimeGreaterThanEqual(getToday());
	}
	public ParkingInfo findByParkingTicketId(int parkingInfoId) {
		return parkingInfoDao.findByParkingInfoId(parkingInfoId);
	}
	
	private LocalDateTime getToday() {
		LocalDate today = LocalDate.now();
		return today.atStartOfDay();
	}
}
