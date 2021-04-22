package com.macro.parking.crawler.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.macro.parking.crawler.PageLoaded;

public class MainPageLoaded extends PageLoaded{
	 
	public MainPageLoaded(String expectedTitle, String expectedUrl) {
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
	    Boolean isCotainDisplayInInfo = false;
	    Boolean isCotainDisplayInTutorial = false;
	    
	    if(isShowAllDivTag(size)) {
	    	isCotainDisplayInInfo = driver.findElement(By.id("information")).getAttribute("style").contains("display");
	 	    isCotainDisplayInTutorial = driver.findElement(By.id("tutorial")).getAttribute("style").contains("display");
	 	   
	    }
	   
	    System.out.println("main");
	    System.out.println(isTitleCorrect + " " + 
	    		isUrlCorrect + " " + getExpectedUrl() + " " +
	    		isTitleCorrect + " " +
	    		isJavascriptLoad + " " +
	    		isCotainDisplayInTutorial + " " + size);
	    return isJavascriptLoad && isTitleCorrect && isUrlCorrect 
	    		&& isShowAllDivTag(size) && isCotainDisplayInInfo && isCotainDisplayInTutorial ;
	  }
	 private Boolean isShowAllDivTag(int size) {
		 	return (size >= 42 || size == 34);
	 }
}
