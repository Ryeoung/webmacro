package com.macro.parking.page.modu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.modu.LoginPageLoaded;

/**
 * 모두의 주차장 로그인 페이지
 */
@Component
public class ModuLoginPage  extends BasePage{
	
    By txtId = By.cssSelector("body > form > div:nth-child(2) > input");    
    By txtPassword = By.cssSelector("body > form > div:nth-child(3) > input");
    By btnLogin = By.cssSelector("body > form > button");

    String url = "https://admin.moduparking.com/";
    String title = "모두의주차장 ADMIN";

    /**
     * url 로드
     */
    public void load() {
		this.navigate(url);
		this.waitForPageLoad(new LoginPageLoaded(title, url));
    }


    /**
     * @param id
     * @param password
     *
     *  유저 로그인 정보 입력
     */
    public void fillUserInfo(String id, String password) {
    	WebElement idElmt = this.waitForElementToAppear(this.txtId);
    	WebElement pwElmt = this.waitForElementToAppear(this.txtPassword);

    	idElmt.sendKeys(id);
        pwElmt.sendKeys(password);
    }

    /**
     * 로그인 버튼 클릭
     */
    public void clickLoginBtn() {
    	WebElement btnElmt = this.waitForElementToAppear(this.btnLogin);
    	btnElmt.click();
    }
}
