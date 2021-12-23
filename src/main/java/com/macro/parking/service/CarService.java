package com.macro.parking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.dao.CarDao;
import com.macro.parking.domain.Car;

/**
 * 차량 객체 service
 */
@Service
public class CarService {
	
	@Autowired
	CarDao carDao;

	/**
	 * @param number 차량 너버
	 * @return Car
	 * 차량 넘버로 차량 객체 찾기
	 */
	public Car findByNumber(String number) {
		return carDao.findByNumber(number);
	}

	/**
	 * @param car 차량 객체
	 * @return Car
	 * 차량 정보 저장
	 */
	public Car addCar(Car car) {
		return carDao.save(car);
	}
}
