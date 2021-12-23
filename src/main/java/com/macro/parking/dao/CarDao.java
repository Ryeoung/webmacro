package com.macro.parking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.Car;

/**
 * 차량 정보를 가져오는 dao
 */
public interface CarDao extends JpaRepository<Car, Long>{
	/**
	 * @param number 차량 넘버
	 * @return Car
	 *  차량 넘버로 차량 데이터를 찾아온다.
	 */
	 Car findByNumber(String number);
}
