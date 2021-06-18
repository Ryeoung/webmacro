package com.macro.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.macro.parking.crawler.*;
import com.macro.parking.enums.WebSite;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.domain.Car;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;


@Service
public class PageCrawlerService {
	
	@Autowired
	ModuPageCrawler moduPageCrawler;
	
	@Autowired
	IParkingPageCralwer iparkPageCrawler;

	@Autowired
	IptimePageCrawler iptimePageCrawler;
	
	@Autowired
	ParkingLotService parkingLotService;
	
	@Autowired
	ParkingInfoService parkingInfoService;
	
	@Autowired
	ParkingTicketService parkingTicketService;

	@Autowired
	AminoPageCrawler aminoPageCrawler;
	
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
		List<ParkingInfo> sortedParkingInfo = this.sortByParkingLotName(parkingInfos);
		
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
	public List<ParkingInfo> sortByParkingLotName(List<ParkingInfo> parkingInfos) {
		List<ParkingInfo> sortedParkingInfo = new ArrayList<>(parkingInfos);
		Collections.sort(sortedParkingInfo, new Comparator<ParkingInfo>() {

			@Override
			public int compare(ParkingInfo o1, ParkingInfo o2) {
				return o1.getParkingTicket().getParkingLot().getName().
						compareTo(o2.getParkingTicket().getParkingLot().getName());
			}
		});
		return sortedParkingInfo;
	}
	private void addTicketByParkingLot(List<ParkingInfo> list) {
		ParkingInfo parkingInfo = list.get(0);
		ParkingLot parkingLot = parkingInfo.getParkingTicket().getParkingLot();
		String url = parkingLot.getWebsite();

		PageCrawler pageCrawler = null;
		if(WebSite.IPARK.isEqual(url)) {
			pageCrawler = this.applyTicketToIpark(list, parkingLot);
		} else if(WebSite.IPTIME.isEqual(url)) {
			pageCrawler = this.applyTicketToIptime(list, parkingLot);
		} else if(WebSite.아미노.isEqual(url)){
			pageCrawler = this.applyTicketToAmino(list, parkingLot);
		} else {
			return;
		}

		pageCrawler.quit();
		parkingInfoService.updateAllParkingInfo(list);

	}

	private PageCrawler applyTicketToIptime(List<ParkingInfo> list, ParkingLot parkingLot) {
		System.out.println(parkingLot.getName());
		iptimePageCrawler.setupChromeDriver();
		iptimePageCrawler.load(parkingLot.getWebsite());
		iptimePageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		iptimePageCrawler.goToApplyTab();
		iptimePageCrawler.applyTickets(list);
		return iptimePageCrawler;
   	}

	private PageCrawler applyTicketToIpark(List<ParkingInfo> list, ParkingLot parkingLot) {
		System.out.println(parkingLot.getName());
		iparkPageCrawler.setupChromeDriver();
		iparkPageCrawler.load(parkingLot.getWebsite());
		iparkPageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		iparkPageCrawler.applyParkingTicket(list);
		return iparkPageCrawler;
	}

	private PageCrawler applyTicketToAmino(List<ParkingInfo> list, ParkingLot parkingLot) {
		System.out.println(parkingLot.getName());
		aminoPageCrawler.setupChromeDriver();
		aminoPageCrawler.load(parkingLot.getWebsite());
		aminoPageCrawler.login(parkingLot.getWebId(), parkingLot.getWebPwd());
		aminoPageCrawler.applyParkingTicket(list);
		return aminoPageCrawler;
	}
	
}
