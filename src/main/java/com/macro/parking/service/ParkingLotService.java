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
	
	public List<ParkingLot> findByNameLike(String name) {
		String query = "%" + name + "%";
		return parkingLotDao.findByNameLike(query);
	}
	
	public ParkingLot findByName(String name) {
		return parkingLotDao.findByName(name);
	}
	
}
