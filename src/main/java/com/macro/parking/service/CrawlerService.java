package com.macro.parking.service;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.dto.CarInfoDto;

@Service
public class CrawlerService {
	@Autowired
	WebCrawler crawler;
	
//	@Autowired
//	WebDriver driver;
//	
	public List<CarInfoDto> getDataFromModu() {
		crawler.setWebDriver();
		return crawler.getDataFromModu();
	}
}
