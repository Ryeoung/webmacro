package com.macro.parking.pageloaded;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class PageLoaded implements ExpectedCondition<Boolean> {		
	  String expectedTitle;
	  String expectedUrl;
		
	  public PageLoaded(String expectedTitle, String expectedUrl) {
	    this.expectedTitle = expectedTitle;	
	    this.expectedUrl = expectedUrl;
	  }
	  
	  public String getExpectedTitle() {
		return expectedTitle;
	}

	public void setExpectedTitle(String expectedTitle) {
		this.expectedTitle = expectedTitle;
	}

	public String getExpectedUrl() {
		return expectedUrl;
	}

	public void setExpectedUrl(String expectedUrl) {
		this.expectedUrl = expectedUrl;
	}

	@Override
	 public Boolean apply(WebDriver  driver) {
		return isDomAttachPage(driver);
	 }
	
	public Boolean isDomAttachPage(WebDriver driver) {
		Boolean isJavascriptLoad = ((JavascriptExecutor) driver).executeScript(
                "return document.readyState"
        ).equals("complete");
	    Boolean isTitleCorrect = driver.getTitle()
	                                   .contains(expectedTitle);
	    Boolean isUrlCorrect = driver.getCurrentUrl()
	                                 .contains(expectedUrl);	
	    
	    return isJavascriptLoad && isTitleCorrect && isUrlCorrect;
	}

}