package com.macro.parking.controller;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.dao.ParkingInfoDao;
import com.macro.parking.dao.ParkingLotDao;
import com.macro.parking.dao.ParkingTicketDao;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.service.CrawlerService;
import com.macro.parking.service.ParkingInfoService;

@RestController
@RequestMapping("/api")
public class MainController {
	@Autowired
	CrawlerService crawlerService;
	
	@Autowired
	ParkingInfoService parkingInfoService;
	
	@Autowired
	ParkingLotDao dao;
	
	@ResponseBody
	@GetMapping("/cars")
	public List<CarInfoDto> getCarInfo() {
		List<ParkingInfo > parkingInfos = parkingInfoService.findAllByToday();
		
		System.out.println(parkingInfos.size());
		return crawlerService.convertParkingInfoToCarInfoDto(parkingInfos);
	}
	
	@ResponseBody
	@GetMapping("/newcars")
	public List<CarInfoDto> getCarsBylast(@RequestParam(defaultValue = "-1") int id) {
		ParkingInfo parkingInfo = null;
		if(id > 0 ) {
			parkingInfo = parkingInfoService.findByParkingTicketId(id);
		} else {
			parkingInfo = parkingInfoService.findLastParkingTicket();
		}
		List<CarInfoDto> carList = crawlerService.getDataFromModu(parkingInfo);
		List<ParkingInfo> parkingInfos = crawlerService.convertCarInfoDtoToParkingInfo(carList);
		parkingInfoService.addAllTicket(parkingInfos);
		
		System.out.println(carList.size());
		return carList;
	}
	
	@PostMapping("/register")
	public List<CarInfoDto> addTicket(@RequestBody List<CarInfoDto> carInfoDto) {
		List<CarInfoDto> carList = crawlerService.pushTicketToParkWebsite(carInfoDto);
		List<ParkingInfo> parkingInfos = crawlerService.convertCarInfoDtoToParkingInfo(carList);
		parkingInfoService.updateByAppFlag(parkingInfos);
		
		System.out.println(carList.size());
		return carList;
	}	
}
