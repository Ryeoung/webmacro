package com.macro.parking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.CarInfoDto;

import com.macro.parking.crawler.ModuPageCrawler;



@Service
public class PageCrawlerService {
	
	@Autowired
	ModuPageCrawler moduPageCrawler;
	
   	public List<CarInfoDto> getParkingTicketReservation(ParkingInfo lastParkingInfo) {
			moduPageCrawler.setupChromeDriver();
			moduPageCrawler.login();
			List<CarInfoDto> parkingTicketReservations = moduPageCrawler.getParkingTicketData(lastParkingInfo);
			moduPageCrawler.quit();
			return parkingTicketReservations;
	}
	
}
