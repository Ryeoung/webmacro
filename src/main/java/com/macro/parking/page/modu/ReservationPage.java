package com.macro.parking.page.modu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.macro.parking.domain.Car;
import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingLot;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.enums.StatusCodeType;
import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.modu.TicketReservationInfoPageLoaded;

/**
 * 주차권 예약 페이지
 */
@Component
public class ReservationPage extends BasePage{
	String rowXPathExp = "/html/body/div/ui-view/partner/table[2]/tbody/tr";

	By pageBtns = By.cssSelector("nav > ul > li.ng-scope"); 
	By reservation = By.xpath( this.rowXPathExp);
	By anotherPageBtn = By.cssSelector("body > div > ui-view > partner > pagination > nav > ul > li:nth-last-child(1) > span");
	
		
	List<WebElement> pageBtnElmts;
	List<WebElement> reservationElmts;
	ParkingInfo firstDataOfPage;
	// >
	WebElement loadAnotherPageBtn;
	
	String url = "https://admin.moduparking.com/main#/partner";
	String title = "모두의주차장 ADMIN";
	
	private int curPage;
	private boolean isFinished;

	/**
	 * 주차권 예약 페이지 로드
	 */
	public void load() {
		this.waitForPageLoad(new TicketReservationInfoPageLoaded(this.title, this.url));
		this.curPage = 1;
		this.loadAnotherPageBtn = this.waitForElementToBeClickAble(this.anotherPageBtn);
		this.waitForPageBtnElmtsToApper();
		this.firstDataOfPage = getFirstDataOfPage();
		this.isFinished = false;
		
		
	}

	/**
	 * 예약 페이지 pagenavigate 번호 로드까지 확인
	 */
	public void waitForPageBtnElmtsToApper() {
		this.pageBtnElmts = this.waitForElementsToAppear(this.pageBtns);
	
	}

	/**
	 * @return ParkingInfo
	 *  첫번째 줄 예약 정보 가져오기
	 */
	public ParkingInfo getFirstDataOfPage() {
        return this.convertReservationElmtToCarInfoDto(0);
	}

	/**
	 * 예약 정보 html element 로드까지 확인
	 */
	public void waitForReservationElmtsToApper() {
		this.reservationElmts = this.waitForElementsToAppear(this.reservation);
		
	}

	/**
	 * @throws InterruptedException
	 * 다음 페이지 버튼 클릭
	 */
	public void clickNextPageBtn() throws InterruptedException {
		if(this.isFinished) {
			return;
		}
		
		if(this.curPage % 5 == 0) {
			this.loadAnotherPageBtn.click();
			this.waitForPageBtnElmtsToApper();
		}
		
		this.curPage += 1;
		this.clickPageBtnByPageNum(this.curPage);
		this.waitForNextPageLoad();
		
		this.firstDataOfPage = getFirstDataOfPage();
	}

	/**
	 *  다음 예약 정보 페이지가 로드 될때 까지 확인
	 */
	public void waitForNextPageLoad() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\nHH:mm:ss");
		String reservationTimeStr = this.firstDataOfPage.getOrderTime().format(formatter);
		this.waitForTextToDisappear(By.xpath("/html/body/div/ui-view/partner/table[2]/tbody/td[1]/div/span"), reservationTimeStr);
	}

	/**
	 * @param pageNum 페이지 넘버
	 *   페이지 넘버 클릭
	 */
	public void clickPageBtnByPageNum(int pageNum) {
		int  btnIdx = (pageNum - 1) % 5;
		this.waitForElementToBeClickAble(this.pageBtnElmts.get(btnIdx).findElement(By.tagName("a"))).click();
	}

	/**
	 * @param lastParkingInfo 이전 최신 예약 주차 정보
	 * @return List<ParkingInfo>
	 *   첫 예약 주차 정보 데이터 부터
	 *   이전 최신 예약 주차 정보까지 크롤링
	 */
	public List<ParkingInfo> crawlingForReservation(ParkingInfo lastParkingInfo) {

		List<ParkingInfo> parkingInfos = new LinkedList<ParkingInfo>();
		
		LocalDateTime toDayStartTime = LocalDate.now().atStartOfDay();
		this.waitForPageLoad(new TicketReservationInfoPageLoaded(this.title, this.url));
		this.waitForReservationElmtsToApper();
		
         for(int rowIdx = 0; rowIdx < this.reservationElmts.size(); rowIdx++) {
            By txtState = By.xpath(this.rowXPathExp +"["+(rowIdx + 1) +"]"+"/td[7]");
     		String stateStr = this.waitForElementToAppear(txtState).getText();

     		ParkingInfo parkingInfo = this.convertReservationElmtToCarInfoDto(rowIdx);


     		 
     		 if((lastParkingInfo != null && lastParkingInfo.isEqual(parkingInfo))
            		 || parkingInfo.getOrderTime().isBefore(toDayStartTime)) {
            	 this.isFinished = true;
             	break;
             }
             
             if(stateStr.equals("결제취소")) {
                 continue;
             }
             
             parkingInfos.add(0, parkingInfo);
         }
        
        
		return parkingInfos;
	}

	/**
	 * @param rowIdx 줄 넘버
	 * @return ParkingInfo
	 *
	 *  해당 줄 넘버에 예약 정보 토대로 주차 정보 객체(ParkingInfo)로 변환
	 */
	public ParkingInfo convertReservationElmtToCarInfoDto( int rowIdx) {

        By txtReservationTime = By.xpath(this.rowXPathExp  + "["+(rowIdx + 1) +"]"+"/td[1]/div/span");
        By txtParkingLot = By.xpath(this.rowXPathExp + "["+(rowIdx + 1) +"]"+"/td[2]/div/a");
        By txtCarNum = By.xpath(this.rowXPathExp + "["+(rowIdx + 1) +"]"+"/td[3]/div/span");
        By txtParkingTicketName = By.xpath(this.rowXPathExp + "["+(rowIdx + 1) +"]"+"/td[4]/div[2]/div[1]/span");
        
		ParkingInfo parkingInfo = new ParkingInfo();
		
        WebElement reservationTimeElmt = this.waitForElementToAppear(txtReservationTime);
        //날짜
        String dateTimeStr = reservationTimeElmt.getText().replaceAll("\\n", " ");
        LocalDateTime orderTime = LocalDateTime.parse(dateTimeStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
       parkingInfo.setOrderTime(orderTime);
       
       //주차권
       String appName = this.waitForElementToAppear(txtParkingTicketName).getText();
       ParkingTicket parkingTicket = new ParkingTicket();
       parkingTicket.setAppName(appName);
       parkingInfo.setParkingTicket(parkingTicket);
        
       //장소
        String parkingLotName = this.waitForElementToAppear(txtParkingLot).getText();

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(parkingLotName);
        parkingInfo.getParkingTicket().setParkingLot(parkingLot);
        
        //차번호
        String[] carNums = this.waitForElementToAppear(txtCarNum).getText().split("\\n");
        String carNum = carNums[0];
        Car car = new Car();
        car.setNumber(carNum);
        parkingInfo.setCar(car);
        parkingInfo.setAppFlag(StatusCodeType.NOT_WORKING);
        
        return parkingInfo;
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
	
}
