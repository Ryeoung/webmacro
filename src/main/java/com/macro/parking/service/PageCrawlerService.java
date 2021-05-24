package com.macro.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.domain.Car;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.enums.StatusCodeType;
import com.macro.parking.crawler.IParkingPageCralwer;
import com.macro.parking.crawler.ModuPageCrawler;



@Service
public class PageCrawlerService {
	
	@Autowired
	ModuPageCrawler moduPageCrawler;
	
	@Autowired
	IParkingPageCralwer iparkPageCrawler;
	
	@Autowired
	ParkingLotService parkingLotService;
	
	@Autowired
	ParkingInfoService parkingInfoService;
	
	@Autowired
	ParkingTicketService parkingTicketService;
	
	@Autowired
	CarService carService;
	
   	public List<ParkingInfo> getParkingTicketReservation(ParkingInfo lastParkingInfo) {
   			moduPageCrawler.setupChromeDriver();
			moduPageCrawler.load();
   			moduPageCrawler.login();
			List<ParkingInfo> parkingInfos = moduPageCrawler.getParkingTicketData(lastParkingInfo);
			parkingInfos.forEach(parkingInfo -> this.getParkingTicketAndCar(parkingInfo));
			moduPageCrawler.quit();
			return parkingInfos;
	}
   	public void getParkingTicketAndCar(ParkingInfo parkingInfo) {
   		String carNum = parkingInfo.getCar().getNumber();
   		
   		Car car = carService.findByNumber(carNum);

		if(car == null) {
			car = carService.addCar(parkingInfo.getCar());
		}
		ParkingLot parkingLot = parkingLotService.findByName(parkingInfo.getParkingTicket().getParkingLot().getName());
		System.out.println(parkingLot);
		ParkingTicket parkingTicket = parkingTicketService.findByAppNameAndParkingLot(parkingInfo.getParkingTicket().getAppName(),  parkingLot);
		if(parkingTicket == null) {
			System.out.println(carNum);
		}
		parkingInfo.setCar(car);
		parkingInfo.setParkingTicket(parkingTicket);
   	}
   	
   	
   	
	
	public void applyParkingTickets(List<ParkingInfo> parkingInfos){
		List<ParkingInfo> sortedParkingInfo = new ArrayList<>(parkingInfos);
		Collections.sort(sortedParkingInfo, new Comparator<ParkingInfo>() {

			@Override
			public int compare(ParkingInfo o1, ParkingInfo o2) {
				return o1.getParkingTicket().getParkingLot().getName().
						compareTo(o2.getParkingTicket().getParkingLot().getName());
			}
		});
		
		ParkingInfo pre = sortedParkingInfo.get(0);
		ParkingInfo cur = null;
		ParkingLot parkingLot;
		List<ParkingInfo> subList = new ArrayList<>();
		subList.add(pre);
		
		
		for(int idx = 1, fin=sortedParkingInfo.size(); idx < fin; idx++) {
			cur = sortedParkingInfo.get(idx);
			if(pre.getParkingTicket().getParkingLot().getName().equals(cur.getParkingTicket().getParkingLot().getName())) {
				subList.add(cur);
				
			} else {
				this.addTicketByParkingLot(subList);
				subList.clear();
				pre = cur;
				subList.add(pre);
			}
		}
		
		this.addTicketByParkingLot(subList);
		
	}
	
	private void addTicketByParkingLot(List<ParkingInfo> list) {
		ParkingInfo parkingInfo = list.get(0);
//		ParkingLot parkingLot = parkingLotService.findByName(carInfoDto.getParkingLotName());
		ParkingLot parkingLot = parkingInfo.getParkingTicket().getParkingLot();

		System.out.println(parkingLot.getName());
		iparkPageCrawler.setupChromeDriver();
		iparkPageCrawler.load();
		iparkPageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		iparkPageCrawler.applyParkingTicket(list);
		
		//		
//		crawler.addTicketByParkingLot(list, parkingLot.getWebId(), parkingLot.getWebPwd());
//		List<ParkingInfo> parkingInfos = convertAllCarInfoDtoToParkingInfos(list);
		parkingInfoService.updateAllParkingInfo(list);
		iparkPageCrawler.quit();
		
	}
	
}
