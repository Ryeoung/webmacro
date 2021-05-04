package com.macro.parking.controller;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.macro.parking.enums.StatusCodeType;
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
		return crawlerService.convertAllParkingInfoToCarInfoDtos(parkingInfos);
	}
	
	@ResponseBody
	@GetMapping("/newcars")
	public List<CarInfoDto> getCarsBylast(@RequestParam(defaultValue = "-1") int id) {
		ParkingInfo parkingInfo = null;
		if(id > 0 ) {
			parkingInfo = parkingInfoService.findlatelyParkingInfoByToday();
		} else {
			parkingInfo = parkingInfoService.findEarlyParkingInfoByToday();
		}
		System.out.println(parkingInfo.getCar().getNumber());
		List<CarInfoDto> carList = crawlerService.getDataFromModu(parkingInfo);
		List<ParkingInfo> parkingInfos = crawlerService.convertAllCarInfoDtoToParkingInfos(carList);
		parkingInfoService.addAllTicket(parkingInfos);
		
		System.out.println(carList.size());
		return carList;
	}
	
	@PostMapping("/register")
	public List<CarInfoDto> addTicket() {
		
		List<CarInfoDto> carList = null;
		List<ParkingInfo> parkingInfos = parkingInfoService.findAllWillCrawling();
		if(parkingInfos.size() > 0 ) {
			List<CarInfoDto> carInfoDtos = crawlerService.convertAllParkingInfoToCarInfoDtos(parkingInfos);
			carList = crawlerService.pushTicketToParkWebsite(carInfoDtos);
			parkingInfos = crawlerService.convertAllCarInfoDtoToParkingInfos(carList);
//			parkingInfoService.updateAllParkingInfo(parkingInfos);
			
		} else {
			carList = new ArrayList<CarInfoDto>();
		}
		
		System.out.println(carList.size());
		return carList;
	}
	
	@PutMapping("/ticket/{parkingInfoId}")
	public List<CarInfoDto> checkTicket(@PathVariable int parkingInfoId, @RequestBody ParkingInfo parkingInfo) {
		ParkingInfo newParkingInfo = parkingInfoService.findByParkingInfoId(parkingInfoId);
		newParkingInfo.setAppFlag(parkingInfo.getAppFlag());
		parkingInfoService.updateParkingInfo(newParkingInfo);
		CarInfoDto carInfoDto = crawlerService.convertParkingInfoToCarInfoDto(newParkingInfo);
		return Arrays.asList(carInfoDto);
		
	}

}
