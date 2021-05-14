package com.macro.parking.page.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.SearchCarPageLoaded;

public class CarSearchPage extends BasePage{
	By cars = By.className("car-list");
	By choiceCarBtn = By.id("next");
	By linkMainPage = By.id("goMain");
	
	private WebElement carElmt;	
	private List<WebElement> carElmts;
	
	private String url = "members.iparking.co.kr/html/car-search-list.html";
    private String title= "i PARKING - MEMBERS";


	
	public void load(String carNumber) {
        this.waitForPageLoad(new SearchCarPageLoaded(title, url));
        this.carElmts = this.waitForElementsToAppear(cars);
        this.waitForElementsToAppear(this.choiceCarBtn);
        this.waitForElementsToAppear(this.linkMainPage);
        
	}
	
	public boolean isCarInParkingLot(String carNum) throws Exception{
	    	boolean flag = false;

	        // 여기서 못 찾는 건 없다는 것
	        for(int idx = 0, fin = this.carElmts.size(); idx < fin; idx++) {
	        	By car = By.className("#carList > tr:nth-child(" + (idx + 1) + ") > td:nth-child(2)");
	        	this.carElmt = this.waitForElementToAppear(car);
	        	
	        	String parkingCarNum = this.carElmt.getText();
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
		this.waitForElementToBeClickAble(this.choiceCarBtn);
		this.javascriptExcutor.executeScript("document.getElementById('next').click();");
	}
	
	public void clickGoMainBtn() {
		this.waitForElementToBeClickAble(this.linkMainPage);
    	this.javascriptExcutor.executeScript("document.getElementById('goMain').click();");

	}
}
