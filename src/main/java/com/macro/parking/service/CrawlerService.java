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
import com.macro.parking.domain.Car;
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
	
	public List<CarInfoDto> getDataFromModu(ParkingTicket lastTicket) {
		crawler.setWebDriver();
		return crawler.getDataFromModu(lastTicket);
	}
	
	public List<ParkingTicket> convertCarInfoDtoToParkingTicket(List<CarInfoDto> dtos) {
		List<ParkingTicket> tickets = new LinkedList<>();
		ParkingLot parkingLot = null;
		CarInfoDto dto = null;
		Car car = null;
		for(int idx = 0, fin = dtos.size(); idx < fin; idx++) {
			dto = dtos.get(idx);
			car = carDao.findByNumber(dto.getCarNum());

			if(car == null) {
				car = new Car();
				car.setNumber(dto.getCarNum());
				car = carDao.save(car);
			}
			
			parkingLot = parkingLotDao.findByName(dto.getParkingLotName());
			ParkingTicket ticket = new ParkingTicket();
			ticket.setCar(car);
			ticket.setParkingLot(parkingLot);
			ticket.setParkingTicketName(dto.getTicket());
			ticket.setOrderTime(dto.getDate());
			
			if(dto.getCode() != null && dto.getCode().equals(StatusCodeType.TICKET_EXIST_ERROR.getCode())) {
				ticket.setAppFlag(true);
			}
			tickets.add(ticket);
		}
		
		return tickets;
		
	}
	public List<CarInfoDto> convertParkingTicketToCarInfoDto(List<ParkingTicket> tickets) {
		List<CarInfoDto> dtos = new LinkedList<>();
		ParkingLot parkingLot = null;
		ParkingTicket ticket = null;
		CarInfoDto dto = null;
		Car car = null;
		
		for(int idx = 0, fin = tickets.size(); idx < fin; idx++) {
			ticket = tickets.get(idx);
			dto = new CarInfoDto();
			dto.setCarNum(ticket.getCar().getNumber());
			if(ticket.isAppFlag()) {
				dto.setCode(StatusCodeType.TICKET_EXIST_ERROR.getCode());
			}
			dto.setParkingTicketId(ticket.getParkingTicketId());
			dto.setParkingLotName(ticket.getParkingLot().getName());
			dto.setTicket(ticket.getParkingTicketName());
			dto.setDate(ticket.getOrderTime());
			
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
