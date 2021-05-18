package com.macro.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.dto.CarInfoDto;
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
	
	
   	public List<CarInfoDto> getParkingTicketReservation(ParkingInfo lastParkingInfo) {
   			moduPageCrawler.setupChromeDriver();
			moduPageCrawler.load();
   			moduPageCrawler.login();
			List<CarInfoDto> parkingTicketReservations = moduPageCrawler.getParkingTicketData(lastParkingInfo);
			moduPageCrawler.quit();
			return parkingTicketReservations;
	}
   	
	
	public List<CarInfoDto> applyParkingTickets(List<CarInfoDto> carInfos){
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
		ParkingLot parkingLot = parkingLotService.findByName(carInfoDto.getParkingLotName());
		
		System.out.println(parkingLot);
		iparkPageCrawler.setupChromeDriver();
		iparkPageCrawler.load();
		iparkPageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		
		
		//		
//		crawler.addTicketByParkingLot(list, parkingLot.getWebId(), parkingLot.getWebPwd());
//		List<ParkingInfo> parkingInfos = convertAllCarInfoDtoToParkingInfos(list);
//		parkingInfoService.updateAllParkingInfo(parkingInfos);
		
	}
	
}
