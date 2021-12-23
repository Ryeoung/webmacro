package com.macro.parking.pageloaded.modu;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.macro.parking.pageloaded.PageLoaded;

/**
 * 주차권 발권 페이지 로드
 */
public class TicketReservationInfoPageLoaded extends PageLoaded{

	public TicketReservationInfoPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
	}

	/**
	 * @param driver 웹 드라이버
	 * @return Boolean
	 * html 요소드를 살펴 페이지 로드 여부 확인
	 */
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

	    return isDomAttached && (size >= 4) && isNoneDisPlayOfLoadingImg;
	  }


}
