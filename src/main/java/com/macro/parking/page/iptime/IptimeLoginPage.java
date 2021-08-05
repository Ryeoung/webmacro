package com.macro.parking.page.iptime;

import com.macro.parking.page.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

@Component
public class IptimeLoginPage extends BasePage {
    @FindBy(id = "t_userid-inputEl")
    WebElement idTxt;

    @FindBy(id = "t_pwd-inputEl")
    WebElement passwordTxt;

    @FindBy(id = "btn_login-btnInnerEl")
    WebElement loginBtn;

    String url;
    public void load(String url) {
        this.url = url;
        super.navigate(this.url);
    }

    public void fillLoginInfo(String id, String pwd) {
        this.idTxt.sendKeys(id);
        this.passwordTxt.sendKeys(pwd);
    }

    public void clickLoginBtn() {
        this.loginBtn.click();
    }
}
