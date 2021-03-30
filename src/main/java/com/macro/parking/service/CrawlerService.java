package com.macro.parking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.dto.CarInfoDto;

@Service
public class CrawlerService {
	@Autowired
	WebCrawler crawler;
	
	public List<CarInfoDto> getDataFromModu() {
		System.out.println(crawler);
		return crawler.getDataFromModu();
	}
}
