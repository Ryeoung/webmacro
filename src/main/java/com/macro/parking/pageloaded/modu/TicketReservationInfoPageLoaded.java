package com.macro.parking.pageloaded.modu;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.macro.parking.pageloaded.PageLoaded;

public class TicketReservationInfoPageLoaded extends PageLoaded{

	public TicketReservationInfoPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
	}
	
	@Override
	  public Boolean apply(WebDriver  driver) {
	    
	    int size  = 0;
	    Boolean isDomAttached = isDomAttachPage(driver);
	    Boolean isNoneDisPlayOfLoadingImg = false;
	    Boolean isDisplayOfDateTime = false;
	    List<WebElement> cellElmt = null;
	    
	    if(!isDomAttached) {
	    	return false;
	    }
	    
	    try{
	    	size = driver.findElements(By.tagName("div")).size();
	    	cellElmt = driver.findElements(By.className("cell_center"));
	    	if(cellElmt.size() == 0) {
	     		isNoneDisPlayOfLoadingImg = true;
	     	}
	    } catch(StaleElementReferenceException e) {
	    	return false;
	    } catch(Exception e) {
	    	return false;
	    }
	   	   
	    
	   System.out.println("주차권  page");
	   System.out.println( " "+size + isDomAttached + " ");
	    return isDomAttached && (size >= 4) && isNoneDisPlayOfLoadingImg;
	  }


}
