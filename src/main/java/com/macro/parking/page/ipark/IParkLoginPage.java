package com.macro.parking.page.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.stereotype.Component;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.modu.LoginPageLoaded;

@Component
public class IParkLoginPage extends BasePage{
	private By txtId = By.id("id");
	private By txtPassword = By.id("password");
	
    private String url = "https://members.iparking.co.kr/";
    private String title= "i PARKING - MEMBERS";
    
	public void load() {
		super.navigate(this.url);
		super.waitForPageLoad(new LoginPageLoaded(this.title, this.url));
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			
		}
    }

    
    public void fillUserInfo(String id, String pwd) {
    	this.waitForElementToAppear(this.txtId);
    	this.waitForElementToAppear(this.txtPassword);

    	
        this.javascriptExcutor.executeScript("document.getElementById('id').value = '"+id +"';");
    	this.javascriptExcutor.executeScript("document.getElementById('password').value = '"+ pwd +"';");
    	this.waitForFillUserInfo(this.txtId, this.txtPassword, id, pwd);
    	 	    	
    }
    
    private void waitForFillUserInfo(By idLocator, By pwdLocator, String id, String pwd) {
    	
    	wait.until(new ExpectedCondition<Boolean>() {
           	public Boolean apply(WebDriver driver) {
                Boolean isIdCorrect = driver.findElement(idLocator).getAttribute("value").contains(id);
                Boolean isPwCorrect = driver.findElement(pwdLocator).getAttribute("value").contains(pwd);
                
                return isIdCorrect && isPwCorrect;
               }
           });
    }
    
    public void login() {
    	this.javascriptExcutor.executeScript("document.getElementById('login').click();");

    }

}
