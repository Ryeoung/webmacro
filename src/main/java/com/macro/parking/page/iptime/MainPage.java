package com.macro.parking.page.iptime;

import com.macro.parking.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("iptimeMainPage")
public class MainPage extends BasePage {
    @FindBy(xpath = "//*[@id='btn_ParkM-btnInnerEl']/span")
    WebElement lnkApplyTab;

    public void clickLinkToApplyTabBtn() {
        this.lnkApplyTab.click();
        this.driver.switchTo().frame("Input_ParkMAdmin_IFrame");
    }
}
