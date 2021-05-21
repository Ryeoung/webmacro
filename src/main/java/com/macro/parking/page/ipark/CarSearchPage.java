package com.macro.parking.page.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.SearchCarPageLoaded;

@Component
public class CarSearchPage extends BasePage{
	By carList = By.className("car-list");
	By choiceCarBtn = By.id("next");
	By linkMainPage = By.id("goMain");
	By cars = By.xpath("//*[@id=\"carList\"]/tr");
	
	private WebElement carElmt;	
	private List<WebElement> carElmts;
	
	private String url = "members.iparking.co.kr/html/car-search-list.html";
    private String title= "i PARKING - MEMBERS";


	
	public void load() {
        this.waitForPageLoad(new SearchCarPageLoaded(title, url));
        this.waitForElementToAppear(carList);
        this.carElmts = this.waitForElementsToAppear(cars);
                
	}
	
	public boolean isCarInParkingLot(String carNum) throws Exception{
	    	boolean flag = false;
	    	
	    	for(int idx = 0, fin = this.carElmts.size(); idx < fin; idx++) {
	        	By car = By.xpath("//*[@id=\"carList\"]/tr[" + (idx + 1) + "]/td[2]");
	        	System.out.println("waitfor...... car");

	        	this.carElmt = this.waitForElementToAppear(car);
	        	String parkingCarNum = this.carElmt.getText();
	        	System.out.println(parkingCarNum);

	            if(carNum.equals(parkingCarNum) || 
	            		carNum.indexOf(parkingCarNum) >= 0 || 
	            		parkingCarNum.indexOf(carNum) >= 0) {
	            	this.carElmt.click();
	                flag = true;
	                break;
	            }
	        }
	        return flag;
	 }
	
	public void clickChoiceCarBtn() {
		this.javascriptExcutor.executeScript("document.getElementById('next').click();");
	}
	
	public void clickGoMainBtn() {		
		this.javascriptExcutor.executeScript("document.getElementById('headerTitle').click();");

	}
}
