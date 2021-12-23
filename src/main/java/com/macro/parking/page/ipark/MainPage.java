package com.macro.parking.page.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.ipark.MainPageLoaded;

/**
 * 아이파크 메인 페이지
 */
@Component("iparkMainPage")
public class MainPage extends BasePage{
	By carNum = By.id("carNumber");
	
	WebElement inputCarNum;
    String title = "i PARKING - MEMBERS";
    String url= "members.iparking.co.kr/html/home.html";

    /**
     * 페이지 로드
     */
    public void load() {
    	this.waitForPageLoad(new MainPageLoaded(title, url));
        this.deletePopUp();
    }

    /**
     * popup창 제거
     */
    public void deletePopUp() {
    	this.javascriptExcutor.executeScript("document.getElementById('information').style.display = 'none';" +
    			"document.getElementById('tutorial').style.display = 'none';");
    }

    /**
     * @param carNumber
     * 차량 검색
     */
    public void searchCarNum(String carNumber) {
    	inputCarNum = this.waitForElementToAppear(this.carNum);
    	
    	//차번호 4자리 검색칸에 넣기
    	this.javascriptExcutor.executeScript("document.getElementById('carNumber').value = '"+ carNumber + "';" +
    											"document.querySelector('#container > section.sec-inp > div.cont > div > button').click();");


    	
    }

    /**
     * @param parkingLotName 주차장 이름
     *  같은 영업장 주차장 선택
     */
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
