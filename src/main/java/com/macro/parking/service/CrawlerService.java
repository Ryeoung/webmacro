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
	
	public List<CarInfoDto> getDataFromModu(ParkingInfo lastParkingInfo) {
		crawler.setWebDriver();
		return crawler.getDataFromModu(lastParkingInfo);
	}
	
	public List<ParkingInfo> convertCarInfoDtoToParkingInfo(List<CarInfoDto> dtos) {
		List<ParkingInfo> parkingInfos = new LinkedList<>();
		ParkingLot parkingLot = null;
		CarInfoDto dto = null;
		Car car = null;
		ParkingTicket parkingTicket;
		for(int idx = 0, fin = dtos.size(); idx < fin; idx++) {
			dto = dtos.get(idx);
			car = carDao.findByNumber(dto.getCarNum());

			if(car == null) {
				car = new Car();
				car.setNumber(dto.getCarNum());
				car = carDao.save(car);
			}
			
			parkingLot = parkingLotDao.findByName(dto.getParkingLotName());
			parkingTicket = parkingTicketDao.findByAppNameAndPakringLotId(dto.getAppTicketName(), parkingLot.getParkingLotId());
			ParkingInfo parkingInfo = new ParkingInfo();
			parkingInfo.setCar(car);
			parkingInfo.setParkingTicket(parkingTicket);
			parkingInfo.setOrderTime(dto.getDate());
			
			if(dto.getCode() != null && dto.getCode().equals(StatusCodeType.TICKET_EXIST_ERROR.getCode())) {
				parkingInfo.setAppFlag(true);
			}
			parkingInfos.add(parkingInfo);
		}
		
		return parkingInfos;
		
	}
	public List<CarInfoDto> convertParkingInfoToCarInfoDto(List<ParkingInfo> parkingInfos) {
		List<CarInfoDto> dtos = new LinkedList<>();
		ParkingLot parkingLot = null;
		ParkingInfo parkingInfo = null;
		CarInfoDto dto = null;
		Car car = null;

		for(int idx = 0, fin = parkingInfos.size(); idx < fin; idx++) {
			parkingInfo = parkingInfos.get(idx);
			dto = new CarInfoDto();
			dto.setCarNum(parkingInfo.getCar().getNumber());

			if(parkingInfo.isAppFlag()) {
				dto.setCode(StatusCodeType.TICKET_EXIST_ERROR.getCode());
			}
			dto.setParkingInfoId(parkingInfo.getParkingInfoId());
			dto.setParkingLotName(parkingInfo.getParkingTicket().getParkingLot().getName());
			dto.setAppTicketName(parkingInfo.getParkingTicket().getAppName());
			dto.setWebTicketName(parkingInfo.getParkingTicket().getWebName());
			dto.setDate(parkingInfo.getOrderTime());
			
			dtos.add(dto);
		}
		
		return dtos;
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
		
	}
}
