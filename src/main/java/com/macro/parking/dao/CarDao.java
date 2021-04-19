package com.macro.parking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macro.parking.domain.Car;

public interface CarDao extends JpaRepository<Car, Long>{
	 Car findByNumber(String number);
}
