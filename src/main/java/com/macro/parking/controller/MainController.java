package com.macro.parking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.dto.CarInfoDto;

@RestController
@RequestMapping("/api")
public class MainController {
	@Autowired
	WebCrawler crawler;
	
	
	@GetMapping("/cars")
	public List<CarInfoDto> getCarInfo() {
		List<CarInfoDto> carList = crawler.getDataFromModu();
		return carList;
	}
	
//	@PostMapping("/register")
//	public List<CarInfoDto> addTicket(@RequestParam("")) {
//		List<CarInfoDto> carList = crawler.getDataFromModu();
//		return carList;
//	}	
}
