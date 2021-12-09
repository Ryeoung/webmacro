package com.macro.parking.controller;


import java.util.ArrayList;
import java.util.List;

import com.macro.parking.enums.StatusCodeType;
import com.macro.parking.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.TicketDto;
import com.macro.parking.service.PageCrawlerService;
import com.macro.parking.service.ParkingInfoService;
import com.macro.parking.service.ParkingLotService;
import com.macro.parking.service.ParkingTicketService;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 6000)
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
	MapUtils mapUtils;

	@GetMapping("/cars")
	public List<TicketDto> getCarInfo() {
		List<ParkingInfo> parkingInfos = parkingInfoService.findAllByToday();
		System.out.println(parkingInfos.size());
		return mapUtils.convertAllToDto(parkingInfos);
	}
	
	@GetMapping("/new/cars")
	public List<TicketDto> getCarsByRecent() {
		ParkingInfo parkingInfo = parkingInfoService.findlatelyParkingInfoByToday();

		List<ParkingInfo> parkingInfos  = pageCrawlerService.getParkingTicketReservation(parkingInfo);
		parkingInfos = parkingInfoService.addAllTicket(parkingInfos);


		List<TicketDto> carList  = mapUtils.convertAllToDto(parkingInfos);
		System.out.println(carList.size());
		return carList;
	}
	
	@GetMapping("/search")
	public List<TicketDto> getCarInfoDtoBySearchWord(@RequestParam("word") String word) {
		List<ParkingLot> parkingLots = parkingLotService.findByNameLike(word);
		List<ParkingTicket> parkingTickets = parkingTicketService.findByParkingLots(parkingLots);
		List<ParkingInfo> parkingInfos = parkingInfoService.findByParkingTicketAndCar(word, parkingTickets);
		
		System.out.println(parkingInfos.size());
		return  mapUtils.convertAllToDto(parkingInfos);
	}
	
	@GetMapping("/apply/cars")
	public List<TicketDto> getApplyParkingTicket() {
		return applyParkingTicket(null, null);
	}

	@GetMapping("/apply/error/car")
	public List<TicketDto> getApplyErrorParkingTicket() {
		return applyParkingTicket(StatusCodeType.SELENIUM_ERROR, null);
	}


	@GetMapping("/apply/parkingLot/{parkingLotName}")
	public List<TicketDto> getApplyErrorParkingTicket(@PathVariable String parkingLotName) {
		ParkingLot parkingLot = parkingLotService.findByName(parkingLotName);
		System.out.println(parkingLot);
		List<ParkingTicket> parkingTickets = parkingTicketService.findByParkingLot(parkingLot);
		for(ParkingTicket pt: parkingTickets) {
			System.out.println(pt);
		}
		return applyParkingTicket(null, parkingTickets);
	}

	public List<TicketDto> applyParkingTicket(StatusCodeType codeType, List<ParkingTicket> parkingTickets) {
		List<TicketDto> carList = null;
		List<ParkingInfo> parkingInfos = null;
		if(parkingTickets == null) {
			parkingInfos = parkingInfoService.findAllWillCrawling(codeType);
		} else {
			parkingInfos = parkingInfoService.findAllWillCrawling(codeType, parkingTickets);
		}


		if(parkingInfos.size() > 0 ) {
			pageCrawlerService.applyParkingTickets(parkingInfos);
			carList  = mapUtils.convertAllToDto(parkingInfos);

		} else {
			carList = new ArrayList<TicketDto>();
		}
		System.out.println(carList.size());
		return carList;
	}


	@PutMapping("/ticket/{parkingInfoId}")
	public TicketDto checkTicket(@PathVariable int parkingInfoId, @RequestBody ParkingInfo parkingInfo) {
		ParkingInfo newParkingInfo = parkingInfoService.findByParkingInfoId(parkingInfoId);
		newParkingInfo.setAppFlag(parkingInfo.getAppFlag());
		parkingInfoService.updateParkingInfo(newParkingInfo);
		TicketDto ticketDto = mapUtils.convertToDto(newParkingInfo);
		return ticketDto;
	}
}
