package com.macro.parking.page.amino;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.amino.LoginPageLoaded;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class AminoLoginPage extends BasePage {
    By idBy = By.id("userId");
    By passwordBy = By.cssSelector("input[name='userPwd']");
    By loginBtnBy = By.id("btnLogin");

    String title = "서울기록원주차장";
    public void load(String url) {
        this.navigate(url);
        this.waitForPageLoad(new LoginPageLoaded(title, url));
        System.out.println("로드끝 ");
    }

    public void fillLogin(String id, String password) {
        this.waitForElementToAppear(idBy).sendKeys(id);
        this.waitForElementToAppear(passwordBy).sendKeys(password);
    }

    public void clickLoginBtn() {
        this.waitForElementToAppear(loginBtnBy);
        this.waitForElementToBeClickAble(loginBtnBy).click();
    }
}
