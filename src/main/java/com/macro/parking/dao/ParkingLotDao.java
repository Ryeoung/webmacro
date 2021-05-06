package com.macro.parking.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.ParkingLot;
public interface ParkingLotDao extends JpaRepository<ParkingLot, Long> {
	ParkingLot findByName(String name);
	List<ParkingLot> findByNameLike(String name);
}
