package com.macro.parking.controller;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
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


import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.service.PageCrawlerService;
import com.macro.parking.service.ParkingInfoService;
import com.macro.parking.service.ParkingLotService;
import com.macro.parking.service.ParkingTicketService;

@RestController
@RequestMapping("/api")
public class MainController {
	@Autowired
	ParkingInfoService parkingInfoService;
	
	@Autowired
	ParkingLotService parkingLotService;
	
	@Autowired
	ParkingTicketService parkingTicketService;
	
	@Autowired
	PageCrawlerService pageCrawlerService;
	
	@Autowired
	ModelMapper mapper;
	
	@ResponseBody
	@GetMapping("/cars")
	public List<CarInfoDto> getCarInfo() {
		List<ParkingInfo > parkingInfos = parkingInfoService.findAllByToday();
		System.out.println(parkingInfos.size());
		return parkingInfos.stream()
				.map(p -> mapper.map(p, CarInfoDto.class))
				.collect(Collectors.toList());
	}
	
	@ResponseBody
	@GetMapping("/new/cars")
	public List<CarInfoDto> getCarsBylast() {
		ParkingInfo parkingInfo = parkingInfoService.findlatelyParkingInfoByToday();

		List<ParkingInfo> parkingInfos  = pageCrawlerService.getParkingTicketReservation(parkingInfo);
		List<CarInfoDto> carList  = parkingInfos.stream()
							.map(p -> mapper.map(p, CarInfoDto.class))
							.collect(Collectors.toList());
		parkingInfoService.addAllTicket(parkingInfos);
		
		System.out.println(carList.size());
		return carList;
	}
	
	@GetMapping("/search")
	public List<CarInfoDto> getCarInfoDtoBySearchWord(@RequestParam("word") String word) {
		List<ParkingLot> parkingLots = parkingLotService.findByNameLike(word);
		List<ParkingTicket> parkingTickets = parkingTicketService.findByParkingLots(parkingLots);
		List<ParkingInfo> parkingInfos = parkingInfoService.findByParkingTicketAndCar(word, parkingTickets);
		
		System.out.println(parkingInfos.size());
		return  parkingInfos.stream()
				.map(p -> mapper.map(p, CarInfoDto.class))
				.collect(Collectors.toList());		
	}
	
	@GetMapping("/apply/cars")
	public List<CarInfoDto> getApplyParkingTicket() {
		List<CarInfoDto> carList = null;
	
		List<ParkingInfo> parkingInfos = parkingInfoService.findAllWillCrawling();
		if(parkingInfos.size() > 0 ) {
			pageCrawlerService.applyParkingTickets(parkingInfos);			
			carList  = parkingInfos.stream()
					.map(p -> mapper.map(p, CarInfoDto.class))
					.collect(Collectors.toList());
			
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
		CarInfoDto carInfoDto = mapper.map(newParkingInfo, CarInfoDto.class);
		return Arrays.asList(carInfoDto);
		
	}

}
