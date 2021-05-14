package com.macro.parking.service;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.CarInfoDto;

import com.macro.parking.crawler.ModuPageCrawler;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class ModePageCrawler {
	
	@Autowired
	ModuPageCrawler moduPage;
	
   	public List<CarInfoDto> getParkingTicketReservation(ParkingInfo lastParkingInfo){
			moduPage.setupChromeDriver();
			moduPage.login();
			return moduPage.getParkingTicketData(lastParkingInfo);		
	}
	
}
