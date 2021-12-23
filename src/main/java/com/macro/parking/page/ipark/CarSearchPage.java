package com.macro.parking.page.ipark;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.SearchCarPageLoaded;

/**
 * 차량 검색 페이지 객체
 */
@Component
public class CarSearchPage extends BasePage{
	By carList = By.className("car-list");
	By cars = By.xpath("//*[@id=\"carList\"]/tr");
	
	private WebElement carElmt;	
	private List<WebElement> carElmts;
	
	private String url = "members.iparking.co.kr/html/car-search-list.html";
    private String title= "i PARKING - MEMBERS";


	/**
	 * 주차권 페이지 로드
	 */
	public void load() {
        this.waitForPageLoad(new SearchCarPageLoaded(title, url));
        this.waitForElementToAppear(carList);

	}

	/**
	 * @param carNum
	 * @return boolean
	 * 	차량이 존재하는 지 확인
	 */
	public boolean isExistCar(String carNum){
	    	boolean flag = false;
			this.carElmts = this.driver.findElements(this.cars);

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

	/**
	 * 차량 선택 버튼 클릭
	 */
	public void clickChoiceCarBtn() {
		this.javascriptExcutor.executeScript("document.getElementById('next').click();");
	}

	/**
	 * 메인 페이지 버튼 클릭
	 */
	public void clickGoMainBtn() {
		this.javascriptExcutor.executeScript("document.getElementById('headerTitle').click();");

	}
}
