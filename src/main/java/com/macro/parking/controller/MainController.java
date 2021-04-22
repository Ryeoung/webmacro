package com.macro.parking.controller;

import javax.servlet.http.HttpSession;

import java.util.List;

import javax.annotation.Resource;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.dao.ParkingTicketDao;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.service.CrawlerService;
import com.macro.parking.service.ParkingTicketService;

@RestController
@RequestMapping("/api")
public class MainController {
	@Autowired
	CrawlerService crawlerService;
	
	@Autowired
	ParkingTicketService parkingTicketService;
	
	@ResponseBody
	@GetMapping("/cars")
	public List<CarInfoDto> getCarInfo() {
		List<ParkingTicket > tickets = parkingTicketService.findAllByToday();
		
		System.out.println(tickets.size());
		return crawlerService.convertParkingTicketToCarInfoDto(tickets);
	}
	
	@ResponseBody
	@GetMapping("/newcars")
	public List<CarInfoDto> getCarsBylast(@RequestParam(defaultValue = "-1") int id) {
		ParkingTicket ticket = null;
		if(id > 0 ) {
			ticket = parkingTicketService.findByParkingTicketId(id);
		} else {
			ticket = parkingTicketService.findLastParkingTicket();
			System.out.println(ticket);
		}
		List<CarInfoDto> carList = crawlerService.getDataFromModu(ticket);
		List<ParkingTicket> tickets = crawlerService.convertCarInfoDtoToParkingTicket(carList);
		parkingTicketService.addAllTicket(tickets);
		
		System.out.println(carList.size());
		return carList;
	}
	
	@PostMapping("/register")
	public List<CarInfoDto> addTicket(@RequestBody List<CarInfoDto> carInfoDto) {
		List<CarInfoDto> carList = crawlerService.pushTicketToParkWebsite(carInfoDto);
		List<ParkingTicket> tickets = crawlerService.convertCarInfoDtoToParkingTicket(carList);
		parkingTicketService.updateByAppFlag(tickets);
		
		System.out.println(carList.size());
		return carList;
	}	
}
