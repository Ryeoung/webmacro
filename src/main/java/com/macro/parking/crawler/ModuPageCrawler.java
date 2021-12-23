package com.macro.parking.crawler;

import java.util.LinkedList;
import java.util.List;


import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.page.modu.ModuLoginPage;
import com.macro.parking.page.modu.ReservationPage;

import lombok.Getter;
import lombok.Setter;


/**
 * 모두의 주차장에 예약된 주차권을 가지고 오는 클래스
 */
@Setter
@Getter
public class ModuPageCrawler extends PageCrawler{
	private final ModuLoginPage loginPage;
	private final ReservationPage reservationPage;
	
	private String id;
	private String password;

	/**
	 * @param loginPage 로그인 페이지
	 * @param reservationPage 예약된 주차권이 있는 페이지
	 *
	 */
	public ModuPageCrawler(ModuLoginPage loginPage, ReservationPage reservationPage) {
		this.loginPage = loginPage;
		this.reservationPage = reservationPage;
	}

	/**
	 * 크롬 드라이버 주입
	 */
	@Override
	public void setupChromeDriver() {
		super.setupChromeDriver();
		this.loginPage.init(this.driver);
		this.reservationPage.init(this.driver);
	}

	/**
	 * 모두의 주차장 로그인 페이지로 간다.
	 */
	public void load() {
		this.loginPage.load();
	}

	/**
	 * 모두의 주차장 로그인
	 */
	public void login() {
		this.loginPage.fillUserInfo(this.id, this.password);
		this.loginPage.clickLoginBtn();
	}

	/**
	 * @param lastParkingInfo 현재 데이터베이스에 있는 최신 주차권 정보
	 * @return List<ParkingInfo>
	 *     입력 받은 주차권 이후로 예약된 주차권 정보를 크롤링해서 가져온다.
	 */
	public List<ParkingInfo> getParkingTicketData(ParkingInfo lastParkingInfo){
		List<ParkingInfo> totalParkingInfos = new LinkedList<ParkingInfo>();
		List<ParkingInfo> crawledData = null;
		reservationPage.load();
		try {
			
			do{
				System.out.println("주차장 플렛폼 관리페이지에서 주차권 예약 정보 가져오기 ");
				
				Thread.sleep(500);
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
				
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return totalParkingInfos;
	}
    

	

}
