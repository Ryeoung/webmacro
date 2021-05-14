package com.macro.parking.page.modu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.stereotype.Component;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.dto.CarInfoDto;
import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.modu.TicketReservationInfoPageLoaded;

@Component
public class ReservationPage extends BasePage{
	String rowXPathExp = "/html/body/div/ui-view/partner/table[2]/tbody/tr";

	By pageBtns = By.cssSelector("nav > ul > li.ng-scope"); 
	By reservation = By.xpath( this.rowXPathExp);
	By anotherPageBtn = By.cssSelector("body > div > ui-view > partner > pagination > nav > ul > li:nth-last-child(1) > span");
	
		
	List<WebElement> pageBtnElmts;
	List<WebElement> reservationElmts;
	CarInfoDto firstDataOfPage;
	// >
	WebElement loadAnotherPageBtn;
	
	String url = "https://admin.moduparking.com/main#/partner";
	String title = "모두의주차장 ADMIN";
	
	private int curPage;
	private boolean isFinished;
	
	public void load() {
		this.waitForPageLoad(new TicketReservationInfoPageLoaded(this.title, this.url));
		this.curPage = 1;
		this.loadAnotherPageBtn = this.waitForElementToBeClickAble(this.anotherPageBtn);
		this.waitForPageBtnElmtsToApper();
		this.firstDataOfPage = getFirstDataOfPage();
		
		
	}
	
	public void waitForPageBtnElmtsToApper() {
		this.pageBtnElmts = this.waitForElementsToAppear(this.pageBtns);
	
	}
	public CarInfoDto getFirstDataOfPage() {
        return this.convertReservationElmtToCarInfoDto(0);
	}
	
	public void waitForReservationElmtsToApper() {
		this.reservationElmts = this.waitForElementsToAppear(this.reservation);
	}
	
	public void clickNextPageBtn() {
		if(this.isFinished) {
			return;
		}
		
		if(this.curPage % 5 == 0) {
			this.loadAnotherPageBtn.click();
			
		}
		
		this.curPage += 1;
		this.clickPageBtnByPageNum(this.curPage);
		this.waitForNextPageLoad();
		this.firstDataOfPage = getFirstDataOfPage();
	}
	
	public void waitForNextPageLoad() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\nHH:mm:ss");
		String reservationTimeStr = this.firstDataOfPage.getDate().format(formatter);
		this.waitForTextToDisappear(By.xpath("/html/body/div/ui-view/partner/table[2]/tbody/tr[1]/td[2]/div/span/a"), reservationTimeStr);
	}
	
	public void clickPageBtnByPageNum(int pageNum) {
		int  btnIdx = (pageNum - 1) % 5;
		WebElement aTagElmt = this.pageBtnElmts.get(btnIdx).findElement(By.tagName("a"));
		this.waitForElementToBeClickAble(aTagElmt).click();
	}
	
	public List<CarInfoDto> crawlingForReservation(ParkingInfo lastParkingInfo) {
		List<CarInfoDto> carInfoDtos = new LinkedList<CarInfoDto>();
		LocalDateTime toDayStartTime = LocalDate.now().atStartOfDay();
		this.waitForPageLoad(new TicketReservationInfoPageLoaded(this.title, this.url));
		this.waitForReservationElmtsToApper();
		
         for(int rowIdx = 0; rowIdx < this.reservationElmts.size(); rowIdx++) {
             System.out.println(rowIdx);
            By txtState = By.xpath(this.rowXPathExp +"["+(rowIdx + 1) +"]"+"/td[7]");
     		String stateStr = this.waitForElementToAppear(txtState).getText();

             CarInfoDto dto = this.convertReservationElmtToCarInfoDto(rowIdx);
             System.out.println("여");
             //최신 데이터와 비
             if((lastParkingInfo != null && dto.isEqual(lastParkingInfo))
            		 || dto.getDate().isBefore(toDayStartTime)) {
            	 this.isFinished = true;
             	break;
             }
             
             if(stateStr.equals("결제취소")) {
                 continue;
             }
             
             carInfoDtos.add(0, dto);
         }
        
        
        
		return carInfoDtos;
	}
	
	public CarInfoDto convertReservationElmtToCarInfoDto( int rowIdx) {

        CarInfoDto dto = new CarInfoDto();
        By txtReservationTime = By.xpath(this.rowXPathExp  + "["+(rowIdx + 1) +"]"+"/td[1]/div/span");
        By txtParkingLot = By.xpath(this.rowXPathExp + "["+(rowIdx + 1) +"]"+"/td[2]/div/span/a");
        By txtCarNum = By.xpath(this.rowXPathExp + "["+(rowIdx + 1) +"]"+"/td[3]/div/span");
        By txtParkingTicketName = By.xpath(this.rowXPathExp + "["+(rowIdx + 1) +"]"+"/td[4]/div[2]/div[1]/span");
        
		
        WebElement reservationTimeElmt = this.waitForElementToAppear(txtReservationTime);
        //날짜
        String dateTimeStr = reservationTimeElmt.getText().replaceAll("\\n", " ");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        dto.setDate(localDateTime);

        //장소
        
        String parkingLotName = this.waitForElementToAppear(txtParkingLot).getText();
        dto.setParkingLotName(parkingLotName);

        //차번호
        String[] carNums = this.waitForElementToAppear(txtCarNum).getText().split("\\n");
        String carNum = carNums[0];
        dto.setCarNum(carNum);

        //주차권
        String ticketName = this.waitForElementToAppear(txtParkingTicketName).getText();
        dto.setAppTicketName(ticketName);
        
        return dto;
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
	
}
