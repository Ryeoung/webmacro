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
	
	
   	public List<ParkingInfo> getParkingTicketReservation(ParkingInfo lastParkingInfo) {
   			moduPageCrawler.setupChromeDriver();
			moduPageCrawler.load();
   			moduPageCrawler.login();
			List<ParkingInfo> parkingInfos = moduPageCrawler.getParkingTicketData(lastParkingInfo);
			moduPageCrawler.quit();
			return parkingInfos;
	}
   	
	
	public List<ParkingInfo> applyParkingTickets(List<ParkingInfo> parkingInfos){
		List<ParkingInfo> sortedParkingInfo = new ArrayList<>(parkingInfos);

		
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
		return sortedParkingInfo;
	}
	
	private void addTicketByParkingLot(List<ParkingInfo> list) {
		ParkingInfo parkingInfo = list.get(0);
//		ParkingLot parkingLot = parkingLotService.findByName(carInfoDto.getParkingLotName());
		ParkingLot parkingLot = parkingInfo.getParkingTicket().getParkingLot();

		System.out.println(parkingInfo.getParkingTicket().getParkingLot().getName());
		System.out.println(parkingLot.getName());
		iparkPageCrawler.setupChromeDriver();
		iparkPageCrawler.load();
		iparkPageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		iparkPageCrawler.applyParkingTicket(list);
		
		//		
//		crawler.addTicketByParkingLot(list, parkingLot.getWebId(), parkingLot.getWebPwd());
//		List<ParkingInfo> parkingInfos = convertAllCarInfoDtoToParkingInfos(list);
//		parkingInfoService.updateAllParkingInfo(parkingInfos);
		
	}
	
}
