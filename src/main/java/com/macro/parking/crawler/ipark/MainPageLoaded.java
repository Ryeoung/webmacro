package com.macro.parking.crawler.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.macro.parking.crawler.PageLoaded;

public class MainPageLoaded extends PageLoaded{
	 
	public MainPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
	}
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
	    
	    	   
	    System.out.println("main");
	    System.out.println(
	    		isCotainDisplayInTutorial + " " + size);
	    
	    return isDomAttached && isCotainDisplayInInfo && isCotainDisplayInTutorial ;
	  }
}
