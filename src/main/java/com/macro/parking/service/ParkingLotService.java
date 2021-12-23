package com.macro.parking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.dao.ParkingLotDao;
import com.macro.parking.domain.ParkingLot;

@Service
public class ParkingLotService {
	@Autowired
	ParkingLotDao parkingLotDao;

	/**
	 * @param name 주차장 이름
	 * @return List<ParkingLot>
	 *     해당 주차장 이름이 포함된 주차장 객체 다 찾기
	 */
	public List<ParkingLot> findByNameLike(String name) {
		String query = "%" + name + "%";
		return parkingLotDao.findByNameLike(query);
	}

	/**
	 * @param name 주차장 이름
	 * @return ParkingLot
	 * 주차장 이름를 통해 주차장 객체 찾기
	 */
	public ParkingLot findByName(String name) {
		return parkingLotDao.findByName(name);
	}
	
}
