package com.macro.parking.page.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.TicketApplyPageLoaded;

public class TicketApplyPage extends BasePage{
	By myTicketList = By.id("myDcList");
	By emptyMyDcList = By.cssSelector("#myDcList > tr > .empty");
	By confirmPopup = By.cssSelector("#confirmPopup #popupOk");
	By successBuyTicketPopup = By.cssSelector("#alertPopup #popupOk");
	By ticketList = By.id("productList");
	
	
    private String title= "i PARKING - MEMBERS";
    String url = "members.iparking.co.kr/html/discount-ticket-apply.html";
    String ticketName;
	public void load(String ticketName) {
		this.ticketName = ticketName;
		this.waitForPageLoad(new TicketApplyPageLoaded(title, url, ticketName));
		this.waitForElementsToAppear(this.myTicketList);
		
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("myDcList")));

	}
	
	public boolean isHavingMyTicket() {
        List<WebElement> emptyCheck = this.driver.findElements(this.emptyMyDcList);
        if(emptyCheck.size() == 0) {
        	return false;
        } 
        
        return true;
	}
	
	public void buyParkingTicket() throws InterruptedException {
		String ticketXpath = "";
        String ticketName = "";
        
        this.waitForElementsToAppear(this.ticketList);
        
        List<WebElement> saleTickets = driver.findElements(By.xpath(ticketXpath));
        int ticketIdx = 0;
        for(WebElement ticket :saleTickets) {
        	ticketIdx += 1;
        	By ticketNameClassName = By.className("#productList > tr:nth-child(" + ticketIdx +  ") > td:nth-child(1)");
        	ticketName = this.waitForElementToAppear(ticketNameClassName).getText();
        	
        	
        	
            if(this.ticketName.equals(ticketName)) {
            	 //주차권 구입 버튼

            	Thread.sleep(500);
    	    	this.javascriptExcutor.executeScript("document.querySelector('#productList > tr:nth-child("+ ticketIdx + ") > td:nth-child(3) > button').click()");
                //최종 확인 pop 승락 2번
                
    	    	this.waitForElementToBeClickAble(this.confirmPopup).click();
    	    	this.waitForElementToBeClickAble(this.successBuyTicketPopup).click();
                                
                this.waitForBuyTicket();

                break;
            }
           
        }
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
}
