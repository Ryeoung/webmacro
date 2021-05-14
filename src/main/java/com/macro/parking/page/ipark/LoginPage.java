package com.macro.parking.page.ipark;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.modu.LoginPageLoaded;

public class LoginPage extends BasePage{
	private By txtId = By.id("id");
	private By txtPassword = By.id("password");
	
    private String url = "members.iparking.co.kr/";
    private String title= "i PARKING - MEMBERS";
    
	public void load() {
		super.navigate("https://admin.moduparking.com/main#/partner");
		super.waitForPageLoad(new LoginPageLoaded(title, url));
		
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
