package com.macro.parking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

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
		return parkingInfoDao.saveAll(parkingInfos);
	}
	
	public List<ParkingInfo> updateAllParkingInfo(List<ParkingInfo> parkingInfos) {
		for(ParkingInfo info :parkingInfos) {
			updateParkingInfo(info);
		}
		System.out.println("바뀐 사이즈 " + parkingInfos.size());
		return parkingInfoDao.saveAll(parkingInfos);
	}
	
	@Transactional
	public ParkingInfo updateParkingInfo(ParkingInfo parkingInfo) {
		ParkingInfo oldParkingInfo = parkingInfoDao.findByParkingInfoId(parkingInfo.getParkingInfoId());
		if(oldParkingInfo != null) {
			return parkingInfoDao.save(parkingInfo);
		}
		return oldParkingInfo;
	}
	public List<ParkingInfo> findAllWillCrawling(StatusCodeType codeType) {
		List<StatusCodeType> appFlags = this.getStatusCodeTypeOfWillCrawling(codeType);
		return parkingInfoDao.findByAppFlagInAndOrderTimeGreaterThanEqual(appFlags, getToday());
	}

	public List<ParkingInfo> findAllWillCrawling(StatusCodeType codeType, List<ParkingTicket> parkingTickets) {
		List<StatusCodeType> appFlags = this.getStatusCodeTypeOfWillCrawling(codeType);

		return parkingInfoDao.findByAppFlagInAndParkingTicketInAndOrderTimeGreaterThanEqual(appFlags,parkingTickets, getToday());
	}

	public List<StatusCodeType> getStatusCodeTypeOfWillCrawling(StatusCodeType codeType) {
		List<StatusCodeType> appFlags = new ArrayList<>();
		if (codeType == null) {
			appFlags.add(StatusCodeType.NO_CAR_ERROR);
			appFlags.add(StatusCodeType.NOT_WORKING);
			appFlags.add(StatusCodeType.SELENIUM_ERROR);
		} else {
			appFlags.add(codeType);
		}

		return appFlags;
	}
	public ParkingInfo findEarlyParkingInfoByToday() {
		return parkingInfoDao.findTopByOrderTimeGreaterThanEqual(getToday());
	}
	
	public ParkingInfo findlatelyParkingInfoByToday() {
		return parkingInfoDao.findTopByOrderTimeGreaterThanEqualOrderByOrderTimeDescParkingInfoIdDesc(getToday());

	}
	
	public List<ParkingInfo> findAllByToday() {
		return parkingInfoDao.findByOrderTimeGreaterThanEqual(getToday());
	}
	public ParkingInfo findByParkingInfoId(int parkingInfoId) {
		return parkingInfoDao.findByParkingInfoId(parkingInfoId);
	}
	
	public List<ParkingInfo> findByParkingTicketAndCar(String carNum, List<ParkingTicket> parkingTickets) {
		String likeQuery = "%" + carNum + "%";
		return parkingInfoDao.findByOrderTimeGreaterThanEqualAndParkingTicketInOrCar_NumberLike(getToday(), parkingTickets, likeQuery);
	}
	
	private LocalDateTime getToday() {
		LocalDate today = LocalDate.now();
		return today.atStartOfDay();
	}
}
