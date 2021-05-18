package com.macro.parking.page.modu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.modu.LoginPageLoaded;

@Component
public class ModuLoginPage  extends BasePage{
	
    By txtId = By.cssSelector("body > form > div:nth-child(2) > input");    
    By txtPassword = By.cssSelector("body > form > div:nth-child(3) > input");
    By btnLogin = By.cssSelector("body > form > button");

    String url = "https://admin.moduparking.com/";
    String title = "모두의주차장 ADMIN";
    public void load() {
		this.navigate(url);
		this.waitForPageLoad(new LoginPageLoaded(title, url));
		System.out.println("로드끝 ");
    }

    
    public void fillUserInfo(String txtId, String txtPassword) {
    	WebElement idElmt = this.waitForElementToAppear(this.txtId);
    	WebElement pwElmt = this.waitForElementToAppear(this.txtPassword);

    	idElmt.sendKeys(txtId);
        pwElmt.sendKeys(txtPassword);
    }
    public void login() {
    	WebElement btnElmt = this.waitForElementToAppear(this.btnLogin);
    	btnElmt.click();
    }
}
