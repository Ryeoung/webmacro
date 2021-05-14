package com.macro.parking.page.modu;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.CarInfoDto;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ModuPage{
	private final LoginPage loginPage;
	private final ReservationPage reservationPage;
	
	private String id;
	private String password;

	public ModuPage(LoginPage loginPage, ReservationPage reservationPage) {
		this.loginPage = loginPage;
		this.reservationPage = reservationPage;
	}

	public void setDriver(WebDriver driver) {
		this.loginPage.init(driver);
		this.reservationPage.init(driver);
		
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
				totalCrawledData.addAll(crawledData);
			}
			reservationPage.clickNextPageBtn();
			
		} while(!reservationPage.isFinished());
		
		
		return crawledData;
	}
    

	

}
