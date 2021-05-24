package com.macro.parking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.dao.CarDao;
import com.macro.parking.domain.Car;

@Service
public class CarService {
	
	@Autowired
	CarDao carDao;
	
	public Car findByNumber(String number) {
		return carDao.findByNumber(number);
	}
	
	public Car addCar(Car car) {
		return carDao.save(car);
	}
}
