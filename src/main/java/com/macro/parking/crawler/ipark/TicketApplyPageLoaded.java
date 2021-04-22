package com.macro.parking.crawler.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.macro.parking.crawler.PageLoaded;

public class TicketApplyPageLoaded  extends PageLoaded{
	
	public TicketApplyPageLoaded(String expectedTitle, String expectedUrl) {
		super(expectedTitle, expectedUrl);
		// TODO Auto-generated constructor stub
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
	    
	    System.out.println("ticket applys");
	    System.out.println(isTitleCorrect + " " + 
	    		isUrlCorrect + " " + getExpectedUrl() + " " +
	    		isTitleCorrect + " " +
	    		isJavascriptLoad + " " +
	    		isFinishedLoading + " " + size);
	    
	     return isJavascriptLoad && isTitleCorrect && isUrlCorrect 
	    		&& (size >= 26) && isFinishedLoading ;
	  }
}
