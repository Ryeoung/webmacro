package com.macro.parking.pageloaded.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.macro.parking.pageloaded.PageLoaded;


/**
 * 메인 페이지 로드
 */
public class MainPageLoaded extends PageLoaded{
	 
	public MainPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
	}

	/**
	 * @param driver
	 * @return Boolean
	 * html 요소드를 살펴 페이지 로드 여부 확인
	 */
	 @Override
	  public Boolean apply(WebDriver  driver) {
		
		    Boolean isDomAttached = isDomAttachPage(driver);
		    int size = 0;
		    Boolean isCotainDisplayInInfo = false;
		    Boolean isCotainDisplayInTutorial = false;
		    
		    try{
		    	size = driver.findElements(By.tagName("div")).size();
		    	isCotainDisplayInInfo = driver.findElement(By.id("information")).getAttribute("style").contains("display");
		 	    isCotainDisplayInTutorial = driver.findElement(By.id("tutorial")).getAttribute("style").contains("display");
		    } catch(StaleElementReferenceException e) {
		    	return false;
		    }
	    
	    return isDomAttached && isCotainDisplayInInfo && isCotainDisplayInTutorial ;
	  }
}
