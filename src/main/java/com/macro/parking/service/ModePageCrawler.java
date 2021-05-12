package com.macro.parking.service;

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

import com.macro.parking.page.modu.ModuPage;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class ModePageCrawler {
	
	@Autowired
	ModuPage moduPage;
	
	public List<CarInfoDto> getParkingTicketReservation(ParkingInfo lastParkingInfo){
		moduPage.setDriver(this.setupChromeDriver());
		moduPage.login();
		return moduPage.getParkingTicketData(lastParkingInfo);
	}
	
	public WebDriver setupChromeDriver()  {
	      //System.setProperty(driverName, path);
	  	  WebDriver driver = null;

		try {
	      WebDriverManager.chromedriver();
	      
	      ChromeOptions options = new ChromeOptions();
	      options.addArguments("--window-size=1366,768");
	      //options.addArguments("--headless");
	      options.setProxy(null);
	      options.setPageLoadStrategy(PageLoadStrategy.EAGER);
	      DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	      driver = new ChromeDriver(capabilities);
		} catch(Exception e) {
			
		}
	      return driver;
	  }
}
