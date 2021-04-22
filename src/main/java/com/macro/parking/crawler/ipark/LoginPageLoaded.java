package com.macro.parking.crawler.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.macro.parking.crawler.PageLoaded;

public class LoginPageLoaded extends PageLoaded{
	 public LoginPageLoaded(String expectedTitle, String expectedUrl) {
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

	   Boolean isSkip = driver.findElement(By.id("skip")).getText().contains("Skip");
	    return isJavascriptLoad && isTitleCorrect && isUrlCorrect && (size >= 13);
	  }

}
