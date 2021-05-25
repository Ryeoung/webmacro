package com.macro.parking.crawler;

import java.util.List;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.enums.StatusCodeType;
import com.macro.parking.page.ipark.CarSearchPage;
import com.macro.parking.page.ipark.IParkLoginPage;
import com.macro.parking.page.ipark.MainPage;
import com.macro.parking.page.ipark.TicketApplyPage;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IParkingPageCralwer extends PageCrawler{
	private final IParkLoginPage loginPage;
	private final MainPage mainPage;
	private final CarSearchPage carSearchPage;
	private final TicketApplyPage ticketApplyPage;
	

	public IParkingPageCralwer(IParkLoginPage loginPage, MainPage mainPage, CarSearchPage carSearchPage,
			TicketApplyPage ticketApplyPage) {
		super();
		this.loginPage = loginPage;
		this.mainPage = mainPage;
		this.carSearchPage = carSearchPage;
		this.ticketApplyPage = ticketApplyPage;
	}
	
	@Override
	public void setupChromeDriver() {
		super.setupChromeDriver();
		
		this.loginPage.init(this.driver);
		this.mainPage.init(this.driver);
		this.carSearchPage.init(this.driver);
		this.ticketApplyPage.init(this.driver);
	}
	
	public void load(String url) {
		loginPage.load(url);
	}
	
	public void login(String id, String pwd) {
		loginPage.fillUserInfo(id, pwd);

		loginPage.login();
	}
	

	public void applyParkingTicket(List<ParkingInfo> parkingInfos) {
		int idx = 0;
		
		String parkingLotName = parkingInfos.get(0).getParkingTicket().getParkingLot().getName();
		
		ParkingInfo parkingInfo = null;
		
		try {
			for(idx = 0; idx < parkingInfos.size(); idx++) {
                parkingInfo  = parkingInfos.get(idx);

				mainPage.load();
				mainPage.deletePopUp();
				
				if(idx == 0) {
					mainPage.selectParkingLot(parkingLotName);
				}
				
                Thread.sleep(500);
				String carNumber = parkingInfo.getCar().getNumber();
	            String fourNumOfCar = carNumber.substring(carNumber.length() - 4, carNumber.length());
	            
	            System.out.println(fourNumOfCar);
				
	            mainPage.searchCarNum(fourNumOfCar);
				
				this.carSearchPage.load();
				
				if(this.carSearchPage.isCarInParkingLot(carNumber)) {
					//차 있는 경우 
					//해당 차량 주차권 정보로 가기
					this.carSearchPage.clickChoiceCarBtn();
					
					this.ticketApplyPage.load(parkingInfo.getParkingTicket().getWebName());
					if(!this.ticketApplyPage.isHavingMyTicket()) {
						//해당 차량의 주차권이 이미 존재하는 경우 
                        parkingInfo.setAppFlag(StatusCodeType.TICKET_EXIST_ERROR);
					} else {
						if(this.ticketApplyPage.buyParkingTicket()) {
							//해당 차량이 구매한 주차권을 구매한 경우 
							parkingInfo.setAppFlag(StatusCodeType.SUCCESS);
						}
					}
					this.ticketApplyPage.clickGoHomeBtn();
					
				} else {
					//치 없는 경우 
                    parkingInfo.setAppFlag(StatusCodeType.NO_CAR_ERROR);
                    
                    //검색창으로 되돌아 가기
                    this.carSearchPage.clickGoMainBtn();
				}

				
			} 
		}  catch(Exception e ) {
        	int curIdx = 0;
        	ParkingInfo info = null;

        	if(parkingInfo != null) {
        		curIdx = idx;
        	}
        	for( ;curIdx < parkingInfos.size(); curIdx++) {
        		info = parkingInfos.get(curIdx);
        		info.setAppFlag(StatusCodeType.SELENIUM_ERROR);
        	}
            e.printStackTrace();
        } 
	   
	}

}
