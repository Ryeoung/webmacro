package com.macro.parking.crawler;

import java.util.LinkedList;
import java.util.List;


import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.page.modu.ModuLoginPage;
import com.macro.parking.page.modu.ReservationPage;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class ModuPageCrawler extends PageCrawler{
	private final ModuLoginPage loginPage;
	private final ReservationPage reservationPage;
	
	private String id;
	private String password;

	public ModuPageCrawler(ModuLoginPage loginPage, ReservationPage reservationPage) {
		this.loginPage = loginPage;
		this.reservationPage = reservationPage;
	}
	
	@Override
	public void setupChromeDriver() {
		super.setupChromeDriver();
		this.loginPage.init(this.driver);
		this.reservationPage.init(this.driver);
	}
	
	public void load() {
		this.loginPage.load();
	}

	public void login() {
		this.loginPage.fillUserInfo(this.id, this.password);
		this.loginPage.login();
	}
	
	public List<ParkingInfo> getParkingTicketData(ParkingInfo lastParkingInfo){
//		List<CarInfoDto> totalCrawledData = new LinkedList<CarInfoDto>();
		List<ParkingInfo> totalParkingInfos = new LinkedList<ParkingInfo>();
		List<ParkingInfo> crawledData = null;
		reservationPage.load();
		
		do{
			System.out.println("주차장 플렛폼 관리페이지에서 주차권 예약 정보 가져오기 ");
			crawledData = reservationPage.crawlingForReservation(lastParkingInfo);
			
			if(crawledData.size() > 0) {
				totalParkingInfos.addAll(0,crawledData);
			}
			System.out.println(reservationPage.isFinished());
			System.out.println("다음 페이지 ");
			try {
				reservationPage.clickNextPageBtn(); 
			} catch(Exception e) {
				
			}
			
			
		} while(!reservationPage.isFinished());
		
		
		return totalParkingInfos;
	}
    

	

}
