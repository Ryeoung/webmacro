package com.macro.parking.crawler.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.macro.parking.crawler.PageLoaded;

public class LoginPageLoaded extends PageLoaded{
	 public LoginPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
		// TODO Auto-generated constructor stub
	}

	 @Override
	  public Boolean apply(WebDriver  driver) {
	    
	    int size  = 0 ;
	    Boolean isIntroDisplay = false;
	    Boolean isSkip = false;
	    Boolean isDomAttached = isDomAttachPage(driver);
	    if(!isDomAttached) {
	    	return false;
	    }
	    Boolean isBlockDisplay = false;
	    try{
	    	size = driver.findElements(By.tagName("div")).size();
	     	isIntroDisplay = driver.findElement(By.id("intro")).getAttribute("style").contains("display");
	     	if(isIntroDisplay) {
		    	((JavascriptExecutor) driver).executeScript("document.getElementById('intro').style.display = 'none';");
	     	}
	     	isBlockDisplay = driver.findElement(By.id("intro")).getAttribute("style").contains("block");

	     	isSkip = driver.findElement(By.id("skip")).getText().contains("Skip");
	    } catch(StaleElementReferenceException e) {
	    	return false;
	    } catch(Exception e) {
	    	return false;
	    }
	   	   
	    
	   System.out.println("login page");
	   System.out.println(isSkip +" "+size + isIntroDisplay + " " + isBlockDisplay);
	    return isDomAttached && (size >= 13) && isIntroDisplay && !isBlockDisplay;
	  }
	 

}
