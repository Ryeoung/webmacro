package com.macro.parking.page.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.TicketApplyPageLoaded;

/**
 *  아이파크 주차권 발권 페이지
 */
@Component("iparkApplyPage")
public class TicketApplyPage extends BasePage{
	String ticketXpathExp = "//*[@id=\"productList\"]/tr";
	
	By myTicketList = By.id("myDcList");
	By emptyMyDcList = By.cssSelector("#myDcList > tr > .empty");
	By confirmPopup = By.cssSelector("#confirmPopup #popupOk");
	By successBuyTicketPopup = By.cssSelector("#alertPopup #popupOk");
	By ticketList = By.id("productList");
	By tickets = By.xpath(ticketXpathExp);
	
    private String title= "i PARKING - MEMBERS";
    String url = "members.iparking.co.kr/html/discount-ticket-apply.html";
    String ticketName;

	/**
	 * @param ticketName 주차권 이름
	 *   주차권 페이지 로드
	 */
	public void load(String ticketName) {
		this.ticketName = ticketName;
		this.waitForPageLoad(new TicketApplyPageLoaded(title, url, ticketName));
		this.waitForElementsToAppear(this.myTicketList);
		
	}

	/**
	 * @return boolean
	 * 이미 발권된 주차권이 있는 지 확인
	 */
	public boolean isHavingMyTicket() {
		
        List<WebElement> emptyCheck = this.driver.findElements(this.emptyMyDcList);
        if(emptyCheck.size() == 0) {
        	return true;
        } 
        return false;
	}

	/**
	 * @return boolean
	 * @throws InterruptedException
	 *  주차권 발권
	 */
	public boolean applyParkingTicket() throws InterruptedException {
        String ticketName = "";
        this.waitForElementToAppear(this.ticketList);

        
        List<WebElement> saleTickets = this.waitForElementsToAppear(tickets);
        int ticketIdx = 0;
        for(WebElement ticket :saleTickets) {
        	ticketIdx += 1;
        	By ticketNameClassName = By.xpath(this.ticketXpathExp + "[" + ticketIdx +  "]/td[1]");
        	ticketName = this.waitForElementToAppear(ticketNameClassName).getText();

        	
            if(this.ticketName.equals(ticketName)) {
            	 //주차권 구입 버튼

            	Thread.sleep(500);
    	    	this.javascriptExcutor.executeScript("document.querySelector('#productList > tr:nth-child("+ ticketIdx + ") > td:nth-child(3) > button').click()");
                //최종 확인 pop 승락 2번
				Thread.sleep(500);
				this.waitForElementToBeClickAble(this.confirmPopup).click();
				this.waitForConfirm();
    	    	this.waitForElementToBeClickAble(this.successBuyTicketPopup).click();
                                
                this.waitForApplyTicket();
                return true;
            }
           
        }
        
        return false;
	}

	/**
	 * 주차권 발권 확인 pop창 로드 될 때까지 wait
	 */
	private void waitForConfirm() {
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				By confirmMessage = By.id("popMessage");
				return driver.findElement(confirmMessage).getText().contains("적용되었습니다.");

			}
		});
	}

	/**
	 * 주차권 발권 까지 wait
	 */
	public void waitForApplyTicket() {
		By emptyTicketList = this.emptyMyDcList;
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
                List<WebElement> emptyCheck = driver.findElements(emptyTicketList);
                //주차권이 있으면
                if(emptyCheck.size() == 0) {
					return true;
				}

                return false;
			}
		});

	}

	/**
	 * 홈 버튼 클릭
	 */
	public void clickGoHomeBtn(){
    	this.javascriptExcutor.executeScript("document.getElementById('goMain').click();");
	}
}
