package com.macro.parking.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.ParkingLot;
public interface ParkingLotDao extends JpaRepository<ParkingLot, Long> {
	/**
	 * @param name 주차장 이름
	 * @return ParkingLot
	 *  해당 주차장이름과 같은 주차장 정보를 가져온다.
	 */
	ParkingLot findByName(String name);

	/**
	 * @param name 주차장 이름 일부
	 * @return List<ParkingLot>
	 *     해당 주차장 이름 일부가 들어간 모든 주차장 정보를 가져온다.
	 */
	List<ParkingLot> findByNameLike(String name);
}
