package com.macro.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.dao.ParkingLotDao;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.dto.CarInfoDto;

@Service
public class CrawlerService {
	@Autowired
	WebCrawler crawler;
	
	@Autowired
	ParkingLotDao parkingLotDao;
//	@Autowired
//	WebDriver driver;
//	
	public List<CarInfoDto> getDataFromModu() {
		crawler.setWebDriver();
		return crawler.getDataFromModu();
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
		ParkingLot parkingLot = parkingLotDao.findByName(carInfoDto.getParkingLotName());
		System.out.println(parkingLot);
		crawler.setWebDriver();
		crawler.addTicketByParkingLot(list, parkingLot.getWebId(), parkingLot.getWebPwd());
		
	}
}
