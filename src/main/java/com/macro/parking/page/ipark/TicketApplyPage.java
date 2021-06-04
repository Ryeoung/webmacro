package com.macro.parking.page.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.TicketApplyPageLoaded;

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
	public void load(String ticketName) {
		this.ticketName = ticketName;
		this.waitForPageLoad(new TicketApplyPageLoaded(title, url, ticketName));
		this.waitForElementsToAppear(this.myTicketList);
		
	}
	
	public boolean isHavingMyTicket() {
		
        List<WebElement> emptyCheck = this.driver.findElements(this.emptyMyDcList);
        if(emptyCheck.size() == 0) {
        	System.out.println("주차권 없음 ");
        	return false;
        } 
        System.out.println("주차권 있음 ");
        return true;
	}
	
	public boolean buyParkingTicket() throws InterruptedException {
		String ticketXpath = "";
        String ticketName = "";
        System.out.println("주차권 사기 진입 ");
        this.waitForElementToAppear(this.ticketList);
        System.out.println("해당 주차장 주차권 로드  ");

        
        List<WebElement> saleTickets = this.waitForElementsToAppear(tickets);
        int ticketIdx = 0;
        for(WebElement ticket :saleTickets) {
        	ticketIdx += 1;
        	By ticketNameClassName = By.xpath(this.ticketXpathExp + "[" + ticketIdx +  "]/td[1]");
            System.out.println("주차권 찾는 중 ");
        	ticketName = this.waitForElementToAppear(ticketNameClassName).getText();
            System.out.println("주차권 있음 " + ticketName);
        	
        	
            if(this.ticketName.equals(ticketName)) {
            	 //주차권 구입 버튼

            	Thread.sleep(500);
    	    	this.javascriptExcutor.executeScript("document.querySelector('#productList > tr:nth-child("+ ticketIdx + ") > td:nth-child(3) > button').click()");
                //최종 확인 pop 승락 2번
				Thread.sleep(500);
				this.waitForElementToBeClickAble(this.confirmPopup).click();
				this.waitForConfirm();
    	    	this.waitForElementToBeClickAble(this.successBuyTicketPopup).click();
                                
                this.waitForBuyTicket();
                return true;
            }
           
        }
        
        return false;
	}

	private void waitForConfirm() {
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				By confirmMessage = By.id("popMessage");
				return driver.findElement(confirmMessage).getText().contains("적용되었습니다.");

			}
		});
	}

	public void waitForBuyTicket() {
		By emptyTicketList = this.emptyMyDcList;
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
                List<WebElement> emptyCheck = driver.findElements(emptyTicketList);
				if(emptyCheck.size() == 0) {
					return false;
				}
                return true;
			}
		});

	}
	
	public void clickGoHomeBtn(){
    	this.javascriptExcutor.executeScript("document.getElementById('goMain').click();");
	}
}
