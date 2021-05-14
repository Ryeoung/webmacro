package com.macro.parking.pageloaded.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.macro.parking.pageloaded.PageLoaded;


public class SearchCarPageLoaded extends PageLoaded{
	public SearchCarPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
		
	}
	
	 @Override
	  public Boolean apply(WebDriver  driver) {
	    Boolean isDomAttached = isDomAttachPage(driver);

	    int size = 0;
	    Boolean isLoadingTagDisplay;
	    Boolean isFinishedLoading = false;
	    Boolean isExistcarInfoChild = false;
	    try {
	    	 size = driver.findElements(By.tagName("div")).size();
	    	 if(driver.findElements(By.className("loading")).size() <= 0) {
	    		 return false;
	    	 }
	    		 
	 	    isLoadingTagDisplay = driver.findElement(By.className("loading")).getAttribute("style").contains("none");
	 	    if(isLoadingTagDisplay) {
	 	    	isFinishedLoading = ((JavascriptExecutor) driver).executeScript(
	 		               "return document.getElementsByClassName('loading')[0].style.display"
	 		       ).equals("none");
	 	    	
	 	    }
	 	   List<WebElement> carInfoChild = driver.findElements(By.cssSelector(".car-info > div"));
	 	   if(carInfoChild.size() == 2) {
	 		  isExistcarInfoChild = true;
	 	   }
	 	    
	    } catch(StaleElementReferenceException e) {
	    	e.printStackTrace();
	    	return false;
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	return false;
	    }

	    
		    System.out.println("car search");
	    System.out.println(
	    		isFinishedLoading + " " + size + " " + isExistcarInfoChild);
	    
	     return isDomAttached && (size == 20) && isFinishedLoading  && isExistcarInfoChild;
	  }


}
