package com.macro.parking.page.amino;

import com.macro.parking.page.BasePage;
import com.macro.parking.pageloaded.amino.LoginPageLoaded;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

/**
 * 아미노 관리 페이지 객체
 */
@Component
public class AminoLoginPage extends BasePage {
    By idBy = By.id("userId");
    By passwordBy = By.cssSelector("input[name='userPwd']");
    By loginBtnBy = By.id("btnLogin");

    String title = "서울기록원주차장";

    /**
     * @param url 해당 url로 페이지 로드
     */
    public void load(String url) {
        this.navigate(url);
        this.waitForPageLoad(new LoginPageLoaded(title, url));
    }

    /**
     * @param id
     * @param password
     *  로그인 정보 입력
     */
    public void fillUserInfo(String id, String password) {
        this.waitForElementToAppear(idBy).sendKeys(id);
        this.waitForElementToAppear(passwordBy).sendKeys(password);
    }

    /**
     * 로그인 버튼 누르기
     */
    public void clickLoginBtn() {
        this.waitForElementToAppear(loginBtnBy);
        this.waitForElementToBeClickAble(loginBtnBy).click();
    }
}
