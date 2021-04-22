package com.macro.parking.crawler.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.macro.parking.crawler.PageLoaded;

public class SearchCarPageLoaded extends PageLoaded{

	public SearchCarPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
	}
	
	 @Override
	  public Boolean apply(WebDriver  driver) {
		
	    Boolean isTitleCorrect = driver.getTitle()
	                                   .contains(getExpectedTitle());
	    Boolean isUrlCorrect = driver.getCurrentUrl()
	                                 .contains(getExpectedUrl());	
	    Boolean isJavascriptLoad = ((JavascriptExecutor) driver).executeScript(
	               "return document.readyState"
	       ).equals("complete");
	    int size = driver.findElements(By.tagName("div")).size();
	    
	    Boolean isLoadingTagDisplay = driver.findElement(By.className("loading")).getAttribute("style").contains("display");
	    Boolean isFinishedLoading = false;
	    if(isLoadingTagDisplay) {
	    	isFinishedLoading = ((JavascriptExecutor) driver).executeScript(
		               "return document.getElementsByClassName('loading')[0].style.display"
		       ).equals("none");
	    }
	    
	    System.out.println("car search");
	    System.out.println(isTitleCorrect + " " + 
	    		isUrlCorrect + " " + getExpectedUrl() + " " +
	    		isTitleCorrect + " " +
	    		isJavascriptLoad + " " +
	    		isFinishedLoading + " " + size);
	    
	     return isJavascriptLoad && isTitleCorrect && isUrlCorrect 
	    		&& (size == 20) && isFinishedLoading ;
	  }


}
