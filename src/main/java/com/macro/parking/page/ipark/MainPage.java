package com.macro.parking.page.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.MainPageLoaded;

@Component("iparkMainPage")
public class MainPage extends BasePage{
	By carNum = By.id("carNumber");
	
	WebElement inputCarNum;
    String title = "i PARKING - MEMBERS";
    String url= "members.iparking.co.kr/html/home.html";
    
    public void load() {
    	this.waitForPageLoad(new MainPageLoaded(title, url));
        this.deletePopUp();
    }
    
    public void deletePopUp() {
//    	wait.until(ExpectedConditions.)
    	//wait.until(ExpectedConditions.elementToBeClickable(By.id("goHome"))).click();
    	this.javascriptExcutor.executeScript("document.getElementById('information').style.display = 'none';" +
    			"document.getElementById('tutorial').style.display = 'none';");
    	//wait.until(ExpectedConditions.elementToBeClickable(By.id("start"))).click();
    }
    
    public void searchCarNum(String carNumber) {
    	inputCarNum = this.waitForElementToAppear(this.carNum);
    	
    	//차번호 4자리 검색칸에 넣기
    	this.javascriptExcutor.executeScript("document.getElementById('carNumber').value = '"+ carNumber + "';" +
    											"document.querySelector('#container > section.sec-inp > div.cont > div > button').click();");


    	
    }
    public void selectParkingLot(String parkingLotName) {
    	Select parkingLotSelect = new Select(driver.findElement(By.id("storeSelect")));
        if(parkingLotName.equals("하이파킹 마제스타시티")) {
            parkingLotSelect.selectByValue("8638");
        } else if(parkingLotName.equals("하이파킹 94빌딩")){
            parkingLotSelect.selectByValue("8637");
        } else if(parkingLotName.equals("하이파킹 NH농협캐피탈빌딩")) {
            parkingLotSelect.selectByValue("72943");
        } else if(parkingLotName.equals("하이파킹 오토웨이타워")) {
            parkingLotSelect.selectByValue("72945");
        }
    }
    
    

}
