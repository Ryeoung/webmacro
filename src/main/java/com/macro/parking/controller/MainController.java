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

	/**
	 * @return List<TicketDto>
	 *
	 *  오늘 예약 된 주차권을 모두 가져온다.
	 */
	@GetMapping("/cars")
	public List<TicketDto> getCarInfo() {
		List<ParkingInfo> parkingInfos = parkingInfoService.findAllByToday();
		return mapUtils.convertAllToDto(parkingInfos);
	}

	/**
	 * @return List<TicketDto>
	 *     새롭게 예약된 주차권 업데이트
	 */
	@GetMapping("/new/cars")
	public List<TicketDto> getCarsByRecent() {
		ParkingInfo parkingInfo = parkingInfoService.findlatelyParkingInfoByToday();

		List<ParkingInfo> parkingInfos  = pageCrawlerService.getParkingTicketReservation(parkingInfo);
		parkingInfos = parkingInfoService.addAllTicket(parkingInfos);


		List<TicketDto> carList  = mapUtils.convertAllToDto(parkingInfos);
		System.out.println(carList.size());
		return carList;
	}

	/**
	 * @param word 검색어
	 * @return List<TicketDto>
	 *
	 *     주차장, 주차권, 주차 정보 테이블에서 검색어와 일치된 정보가 있는 지 확인하고
	 *     있으면 가져온다.
	 */
	@GetMapping("/search")
	public List<TicketDto> getCarInfoDtoBySearchWord(@RequestParam("word") String word) {
		List<ParkingLot> parkingLots = parkingLotService.findByNameLike(word);
		List<ParkingTicket> parkingTickets = parkingTicketService.findByParkingLots(parkingLots);
		List<ParkingInfo> parkingInfos = parkingInfoService.findByParkingTicketAndCar(word, parkingTickets);
		return  mapUtils.convertAllToDto(parkingInfos);
	}

	/**
	 * @return List<TicketDto>
	 *     주차장 사이트에 자동으로 주차권을 넣는다.
	 */
	@GetMapping("/apply/cars")
	public List<TicketDto> getApplyParkingTicket() {
		return applyParkingTicket(null, null);
	}

	/**
	 * @return List<TicketDto>
	 *
	 *     자동으로 주차권을 넣는 과정에서 에러가 난 주차권을 다시 넣는다.
	 */
	@GetMapping("/apply/error/car")
	public List<TicketDto> getApplyErrorParkingTicket() {
		return applyParkingTicket(StatusCodeType.SELENIUM_ERROR, null);
	}


	/**
	 * @param parkingLotName 주차장 이름
	 * @return List<TicketDto>
	 *     입력받은 주차장에 예약된 주차권을 넣는다.
	 */
	@GetMapping("/apply/parkingLot/{parkingLotName}")
	public List<TicketDto> getAppliedParkingTicketByParkingLotName(@PathVariable String parkingLotName) {
		ParkingLot parkingLot = parkingLotService.findByName(parkingLotName);
		List<ParkingTicket> parkingTickets = parkingTicketService.findByParkingLot(parkingLot);
		return applyParkingTicket(null, parkingTickets);
	}

	/**
	 * @param codeType 주차권 상태
	 * @param parkingTickets 주차장에 넣을 주차권
	 * @return List<TicketDto>
	 *     예약된 주차권 상태에 따라 현재 넣을 주차권을 받아와 자동으로 주차 관리사이트에 주차권을 넣는다.
	 */
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
		return carList;
	}


	/**
	 * @param parkingInfoId 주차 정보 기본키
	 * @param parkingInfo 주차정보
	 * @return TicketDto
	 * 		입력받은 주차권 상태로 바꾼다.
	 */
	@PutMapping("/ticket/{parkingInfoId}")
	public TicketDto checkTicket(@PathVariable int parkingInfoId, @RequestBody ParkingInfo parkingInfo) {
		ParkingInfo newParkingInfo = parkingInfoService.findByParkingInfoId(parkingInfoId);
		newParkingInfo.setAppFlag(parkingInfo.getAppFlag());
		parkingInfoService.updateParkingInfo(newParkingInfo);
		TicketDto ticketDto = mapUtils.convertToDto(newParkingInfo);
		return ticketDto;
	}
}
