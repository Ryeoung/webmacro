package com.macro.parking.crawler;

import java.util.LinkedList;
import java.util.List;


import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.page.modu.LoginPage;
import com.macro.parking.page.modu.ReservationPage;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class ModuPageCrawler extends PageCrawler{
	private final LoginPage loginPage;
	private final ReservationPage reservationPage;
	
	private String id;
	private String password;

	public ModuPageCrawler(LoginPage loginPage, ReservationPage reservationPage) {
		this.loginPage = loginPage;
		this.reservationPage = reservationPage;
	}
	
	@Override
	public void setupChromeDriver() {
		super.setupChromeDriver();
		this.loginPage.init(this.driver);
		this.reservationPage.init(this.driver);
	}
	

	public void login() {
		this.loginPage.load();
		this.loginPage.fillUserInfo(this.id, this.password);
		this.loginPage.login();
	}
	
	public List<CarInfoDto> getParkingTicketData(ParkingInfo lastParkingInfo) {
		List<CarInfoDto> totalCrawledData = new LinkedList<CarInfoDto>();
		List<CarInfoDto> crawledData = null;
		reservationPage.load();
		
		do{
			
			crawledData = reservationPage.crawlingForReservation(lastParkingInfo);
			
			if(crawledData.size() > 0) {
				totalCrawledData.addAll(0,crawledData);
			}
			reservationPage.clickNextPageBtn();
			
		} while(!reservationPage.isFinished());
		
		
		return totalCrawledData;
	}
    

	

}
