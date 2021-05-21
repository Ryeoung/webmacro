package com.macro.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.dao.CarDao;
import com.macro.parking.dao.ParkingLotDao;
import com.macro.parking.dao.ParkingTicketDao;
import com.macro.parking.domain.Car;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.enums.StatusCodeType;

@Service
public class CrawlerService {
	@Autowired
	WebCrawler crawler;
	
	@Autowired
	ParkingLotDao parkingLotDao;
	
	@Autowired
	CarDao carDao;
	
	@Autowired
	ParkingTicketDao parkingTicketDao;
	
	@Autowired
	ParkingInfoService parkingInfoService;
	
	
	public List<CarInfoDto> getDataFromModu(ParkingInfo lastParkingInfo) {
		crawler.setWebDriver();
		return crawler.getDataFromModu(lastParkingInfo);
	}
	
	public List<ParkingInfo> convertAllCarInfoDtoToParkingInfos(List<CarInfoDto> dtos) {
		List<ParkingInfo> parkingInfos = new LinkedList<>();
		ParkingLot parkingLot = null;
		CarInfoDto carInfoDto = null;
		Car car = null;
		ParkingTicket parkingTicket;
		for(int idx = 0, fin = dtos.size(); idx < fin; idx++) {
			carInfoDto = dtos.get(idx);
		
			ParkingInfo parkingInfo = convertCarInfoDtoToParkingInfo(carInfoDto);

			parkingInfos.add(parkingInfo);
		}
		
		return parkingInfos;
		
	}
	
	public ParkingInfo convertCarInfoDtoToParkingInfo(CarInfoDto carInfoDto) {
		Car car = carDao.findByNumber(carInfoDto.getCarNum());

		if(car == null) {
			car = new Car();
			car.setNumber(carInfoDto.getCarNum());
			car = carDao.save(car);
		}
		System.out.println(carInfoDto);
		ParkingLot parkingLot = parkingLotDao.findByName(carInfoDto.getParkingLotName());
		System.out.println(parkingLot);
		ParkingTicket parkingTicket = parkingTicketDao.findByAppNameAndParkingLot_ParkingLotId(carInfoDto.getAppTicketName(), parkingLot.getParkingLotId());
		ParkingInfo parkingInfo = new ParkingInfo();
		parkingInfo.setCar(car);
		parkingInfo.setParkingTicket(parkingTicket);
		parkingInfo.setOrderTime(carInfoDto.getDate());
		parkingInfo.setParkingInfoId(carInfoDto.getParkingInfoId());
		if(carInfoDto.getCode() != null) {
			if( carInfoDto.getCode().equals(StatusCodeType.TICKET_EXIST_ERROR.getCode())) {
				parkingInfo.setAppFlag(StatusCodeType.TICKET_EXIST_ERROR);
				
			} else if(carInfoDto.getCode().equals(StatusCodeType.SUCCESS.getCode())) {
				parkingInfo.setAppFlag(StatusCodeType.SUCCESS);
				
			} else if(carInfoDto.getCode().equals(StatusCodeType.SELENIUM_ERROR.getCode())) {
				parkingInfo.setAppFlag(StatusCodeType.SELENIUM_ERROR);
				
			} else if(carInfoDto.getCode().equals(StatusCodeType.CHECK_TICKET.getCode())) {
				parkingInfo.setAppFlag(StatusCodeType.CHECK_TICKET);
				
			} else if(carInfoDto.getCode().equals(StatusCodeType.NO_CAR_ERROR.getCode())) {
				parkingInfo.setAppFlag(StatusCodeType.NO_CAR_ERROR);

			} else if(carInfoDto.getCode().equals(StatusCodeType.CANCEL.getCode())) {
				parkingInfo.setAppFlag(StatusCodeType.CANCEL);
				
			} else {
				parkingInfo.setAppFlag(StatusCodeType.NOT_WORKING);
			}
		} else {
			parkingInfo.setAppFlag(StatusCodeType.NOT_WORKING);
		}

		return parkingInfo;
	}
	
	public List<CarInfoDto> convertAllParkingInfoToCarInfoDtos(List<ParkingInfo> parkingInfos) {
		List<CarInfoDto> dtos = new LinkedList<>();
		ParkingLot parkingLot = null;
		ParkingInfo parkingInfo = null;
		CarInfoDto dto = null;
		Car car = null;

		for(int idx = 0, fin = parkingInfos.size(); idx < fin; idx++) {
			parkingInfo = parkingInfos.get(idx);
			dto = convertParkingInfoToCarInfoDto(parkingInfo);
			dtos.add(dto);
		}
		
		return dtos;
	}
	public CarInfoDto convertParkingInfoToCarInfoDto(ParkingInfo parkingInfo) {
		CarInfoDto dto = new CarInfoDto();
//		System.out.println(parkingInfo.getCar().getNumber());
		dto.setCarNum(parkingInfo.getCar().getNumber());
		if(parkingInfo.getAppFlag().isEqual(StatusCodeType.TICKET_EXIST_ERROR)) {
			dto.setCode(StatusCodeType.TICKET_EXIST_ERROR.getCode());
			
		} else if(parkingInfo.getAppFlag().isEqual(StatusCodeType.SUCCESS)) {
			dto.setCode(StatusCodeType.SUCCESS.getCode());
			
		} else if(parkingInfo.getAppFlag().isEqual(StatusCodeType.SELENIUM_ERROR)) {
			dto.setCode(StatusCodeType.SELENIUM_ERROR.getCode());
			
		} else if(parkingInfo.getAppFlag().isEqual(StatusCodeType.NOT_WORKING)){
			dto.setCode(StatusCodeType.NOT_WORKING.getCode());
			
		} else if(parkingInfo.getAppFlag().isEqual(StatusCodeType.CHECK_TICKET)){
			dto.setCode(StatusCodeType.CHECK_TICKET.getCode());
			
		} else if(parkingInfo.getAppFlag().isEqual(StatusCodeType.CANCEL)){
			dto.setCode(StatusCodeType.CANCEL.getCode());
		} else {
			dto.setCode(StatusCodeType.NO_CAR_ERROR.getCode());
		}
		
		System.out.println(dto.getCarNum());			
		dto.setParkingInfoId(parkingInfo.getParkingInfoId());
		dto.setParkingLotName(parkingInfo.getParkingTicket().getParkingLot().getName());
		dto.setAppTicketName(parkingInfo.getParkingTicket().getAppName());
		dto.setWebTicketName(parkingInfo.getParkingTicket().getWebName());
		dto.setDate(parkingInfo.getOrderTime());
		
		return dto;
	}
	
	public List<CarInfoDto> pushTicketToParkWebsite(List<CarInfoDto> carInfos){
		List<CarInfoDto> sortedCarInfos = new ArrayList<>(carInfos);
		Collections.sort(sortedCarInfos, new Comparator<CarInfoDto>() {

			@Override
			public int compare(CarInfoDto o1, CarInfoDto o2) {
				return o1.getParkingLotName().compareTo(o2.getParkingLotName());
			}
		});
		
		CarInfoDto pre = sortedCarInfos.get(0);
		CarInfoDto cur = null;
		ParkingLot parkingLot;
		List<CarInfoDto> subList = new ArrayList<>();
		subList.add(pre);
		
		
		for(int idx = 1, fin=sortedCarInfos.size(); idx < fin; idx++) {
			cur = sortedCarInfos.get(idx);
			if(pre.getParkingLotName().equals(cur.getParkingLotName())) {
				subList.add(cur);
				
			} else {
				this.addTicketByParkingLot(subList);
				subList.clear();
				pre = cur;
				subList.add(pre);
			}
		}
		
		this.addTicketByParkingLot(subList);
		return carInfos;
	}
	
	private void addTicketByParkingLot(List<CarInfoDto> list) {
		CarInfoDto carInfoDto = list.get(0);
		System.out.println(carInfoDto.getParkingLotName());
		ParkingLot parkingLot = parkingLotDao.findByName(carInfoDto.getParkingLotName());
		System.out.println(parkingLot);
		crawler.setWebDriver();
		crawler.addTicketByParkingLot(list, parkingLot.getWebId(), parkingLot.getWebPwd());
		List<ParkingInfo> parkingInfos = convertAllCarInfoDtoToParkingInfos(list);
		parkingInfoService.updateAllParkingInfo(parkingInfos);
		
	}
}
