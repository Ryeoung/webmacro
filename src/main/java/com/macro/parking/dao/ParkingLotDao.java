package com.macro.parking.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.ParkingLot;
public interface ParkingLotDao extends JpaRepository<ParkingLot, Long> {
	ParkingLot findByName(String name);
}
