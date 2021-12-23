package com.macro.parking.page.iptime;

import com.macro.parking.page.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * 아이피타임 로그인 페이지
 */
@Component
public class IptimeLoginPage extends BasePage {
    @FindBy(id = "t_userid-inputEl")
    WebElement idTxt;

    @FindBy(id = "t_pwd-inputEl")
    WebElement passwordTxt;

    @FindBy(id = "btn_login-btnInnerEl")
    WebElement loginBtn;

    String url;

    /**
     * @param url
     * 헤당 페이지 로드
     */
    public void load(String url) {
        this.url = url;
        super.navigate(this.url);
    }

    /**
     * @param id
     * @param pwd
     *  로그인 정보 입력
     */
    public void fillLoginInfo(String id, String pwd) {
        this.idTxt.sendKeys(id);
        this.passwordTxt.sendKeys(pwd);
    }

    /**
     * 로그인 버튼 클릭
     */
    public void clickLoginBtn() {
        this.loginBtn.click();
    }
}
