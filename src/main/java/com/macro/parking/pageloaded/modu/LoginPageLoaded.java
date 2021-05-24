package com.macro.parking.pageloaded.modu;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.macro.parking.pageloaded.PageLoaded;

public class LoginPageLoaded extends PageLoaded{

	public LoginPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
	}
	
	  @Override
	  public Boolean apply(WebDriver  driver) {
	    
	    int size  = 0;
	    Boolean isDomAttached = isDomAttachPage(driver);
	    System.out.println(isDomAttached);
	    if(!isDomAttached) {
	    	return false;
	    }
	    
	    try{
	    	size = driver.findElements(By.tagName("div")).size();
	     	
	    } catch(StaleElementReferenceException e) {
	    	return false;
	    } catch(Exception e) {
	    	return false;
	    }
	   	   
	    
	   System.out.println("login page");
	   System.out.println( " "+size + isDomAttached + " ");
	    return isDomAttached && (size >= 4) ;
	  }
}
