package com.macro.parking.pageloaded;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 페이지 로드 객체
 */
public class PageLoaded implements ExpectedCondition<Boolean> {
	  String expectedTitle;
	  String expectedUrl;
		
	  public PageLoaded(String expectedTitle, String expectedUrl) {
	    this.expectedTitle = expectedTitle;	
	    this.expectedUrl = expectedUrl;
	  }


	/**
	 * @param driver 웹 드라이버
	 * @return Boolean
	 * html 요소드를 살펴 페이지 로드 여부 확인
	 */
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