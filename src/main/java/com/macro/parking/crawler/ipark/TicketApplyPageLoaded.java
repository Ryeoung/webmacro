package com.macro.parking.crawler.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.macro.parking.crawler.PageLoaded;
import com.macro.parking.enums.StatusCodeType;

public class TicketApplyPageLoaded  extends PageLoaded{
	private String ticketName;
	private String ticketXpath = "//*[@id=\"productList\"]/tr";

	public TicketApplyPageLoaded(String expectedTitle, String expectedUrl, String ticketName) {
		super(expectedTitle, expectedUrl);
		this.ticketName = ticketName;
		// TODO Auto-generated constructor stub
	}

	@Override
	  public Boolean apply(WebDriver  driver) {
	    Boolean isDomAttached = isDomAttachPage(driver);
	    Boolean isLoadingTagDisplay = driver.findElement(By.className("loading")).getAttribute("style").contains("display");
	    Boolean isFinishedLoading = false;
	    Boolean isExisitTicket = false;
	    String ticketName = "";
	    int size = 0;
	    try {
	    		size = driver.findElements(By.tagName("div")).size();
	 	    
	 	    	isLoadingTagDisplay = driver.findElement(By.className("loading")).getAttribute("style").contains("display");
	 	    	isFinishedLoading = false;
		 	    if(isLoadingTagDisplay) {
		 	    	isFinishedLoading = ((JavascriptExecutor) driver).executeScript(
		 		               "return document.getElementsByClassName('loading')[0].style.display"
		 		       ).equals("none");
		 	    }
		 	    
	           List<WebElement> saleTickets = driver.findElements(By.xpath(this.ticketXpath));
          		System.out.println(this.ticketName);
           		

	           for(WebElement ticket :saleTickets) {
	        	   ticketName = ticket.findElement(By.xpath("td[1]")).getText();
	               if(this.ticketName.equals(ticketName)) {
	               	 //주차권 구입 버튼\
	            	   isExisitTicket = true;
	                   break;
	               }
	              
	           }
	    } catch(StaleElementReferenceException | NullPointerException e) {
	    	return false;
	    }
	   
	    
	    System.out.println("ticket applys");
	    System.out.println(ticketName + " " +
	    		isFinishedLoading + " " + size + isExisitTicket);
	    
	     return  isDomAttached && (size >= 25) && isFinishedLoading && isExisitTicket;
	  }
}
