package com.macro.parking.page.amino;

import com.macro.parking.page.BasePage;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("aminoTicketApplyPage")
public class TicketApplyPage extends BasePage {
    By modalConfirmOkBtnBy = By.className("modal-btn");
    By carInputBy = By.id("schCarNo");
    By carSearchBtnBy = By.className("btnS1_1 btn");
    By searchedCarNumBy = By.className("cellselected");
    By ticketsBy = By.cssSelector("a[name='btnDscntType']");
    By appliedTicketBy = By.cssSelector("#gridDtl tr.ev_dhx_skyblue > td:nth-child(2)");

    @FindBy(id = "schCarNo")
    WebElement carInputElmt;

    @FindBy(className = "btnS1_1")
    WebElement carSearchBtnElmt;

    List<WebElement> ticketElmts;

    public void load() {
        this.waitForElementToAppear(this.modalConfirmOkBtnBy).click();
    }

    public void searchCar(String carNum) {
        this.carInputElmt.clear();
        this.carInputElmt.sendKeys(carNum);
        this.carSearchBtnElmt.click();
    }

    public boolean isExistCar(String carNum) {
        List<WebElement> searchedCar = this.driver.findElements(searchedCarNumBy);
        if(searchedCar.size() > 0) {
            //있는 경우
            return true;
        }

        this.waitForElementToAppear(this.modalConfirmOkBtnBy).click();
        return false;
    }
    public boolean isHavingTicket(String ticketName) {
        //td[title='ticketName']
        List<WebElement> appliedTicket = this.driver.findElements(this.appliedTicketBy);
        if(appliedTicket.size() > 0) {
            return true;
        }

        return false;
    }
    public boolean applyTicket(String findTicketName) {
        this.ticketElmts = this.waitForElementsToAppear(this.ticketsBy);

        for(WebElement ticketElmt: this.ticketElmts) {
            String ticketName = ticketElmt.getText();
            if(ticketName.equals(findTicketName) ||
                    ticketName.indexOf(findTicketName) >= 0 ||
                    findTicketName.indexOf(ticketName) >= 0){
                ticketElmt.click();
                this.waitForElementToAppear(this.modalConfirmOkBtnBy).click();
                this.waitForApplyTicket(findTicketName);
                return true;
            }
        }

        return false;
    }

    public void waitForApplyTicket(String ticketName){
        By appliedTicketBy = this.appliedTicketBy;
        this.wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> appliedTicket = driver.findElements(appliedTicketBy);
                if(appliedTicket.size() > 0) {
                    return true;
                }

                return false;
            }
        });
    }
}
