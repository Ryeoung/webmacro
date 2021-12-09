package com.macro.parking.crawler;

import java.util.List;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.enums.StatusCodeType;
import com.macro.parking.page.ipark.CarSearchPage;
import com.macro.parking.page.ipark.IParkLoginPage;
import com.macro.parking.page.ipark.MainPage;
import com.macro.parking.page.ipark.TicketApplyPage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 아이파크(주차장 관리사이트)를 동적 크롤링(자동화)하여 주차권을 넣는다.
 */
@Component
@Setter
@Getter
public class IParkingPageCralwer extends PageCrawler{
	private final IParkLoginPage loginPage;
	private final MainPage mainPage;
	private final CarSearchPage carSearchPage;
	private final TicketApplyPage ticketApplyPage;

	/**
	 * @param loginPage 로그인 페이지
	 * @param mainPage 메인 페이지
	 * @param carSearchPage 차량 검색 페이지
	 * @param ticketApplyPage 주차권 넣는 페이지
	 *
	 *   크롤러 초기화 각 페이지 객체를 초기화 해준다.
	 */
	@Autowired
	public IParkingPageCralwer(IParkLoginPage loginPage, @Qualifier("iparkMainPage") MainPage mainPage, CarSearchPage carSearchPage,
							   @Qualifier("iparkApplyPage") TicketApplyPage ticketApplyPage) {
		super();
		this.loginPage = loginPage;
		this.mainPage = mainPage;
		this.carSearchPage = carSearchPage;
		this.ticketApplyPage = ticketApplyPage;
	}

	/**
	 * 각 페이지 객체에 웹 드라이버 주입
	 */
	@Override
	public void setupChromeDriver() {
		super.setupChromeDriver();
		
		this.loginPage.init(this.driver);
		this.mainPage.init(this.driver);
		this.carSearchPage.init(this.driver);
		this.ticketApplyPage.init(this.driver);
	}

	/**
	 * @param url 시작 사이트
	 *
	 *  해당 사이트에 입력 받은 url로 접속한다.
	 */
	public void load(String url) {
		loginPage.load(url);
	}

	/**
	 * @param id 아이디
	 * @param pwd 비밀번호
	 *
	 *  로그인
	 */
	public void login(String id, String pwd) {
		loginPage.fillUserInfo(id, pwd);

		loginPage.login();
	}


	/**
	 * @param parkingInfos 주차 정보
	 *
	 *  현재 주차정보를 가지고 주차권을 넣는다.
	 */
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
					if(this.ticketApplyPage.isHavingMyTicket()) {
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
