package com.macro.parking.page.iptime;

import com.macro.parking.page.BasePage;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component("iptimeApplyPage")
public class TicketApplyPage extends BasePage {
    By carNumTxtExp = By.id("f_carno-inputEl");

    By carSearchBtnExp = By.id("btn_search-btnInnerEl");
    By parkingInCarNumExp = By.cssSelector("table > tbody > tr.x-grid-row > td.x-grid-cell.x-grid-cell-Column3 > div");
    By targetCarNumExp = By.cssSelector("#l_carno > span");
    By applyTicketContainerExp = By.id("c_tiket-inputEl");
    By applyTicketsExp = By.className("x-boundlist-item");
    By applyBtnExp = By.id("btn_save-btnInnerEl");
    By appliedTicketExp = By.cssSelector(" table > tbody > tr.x-grid-row > td.x-grid-cell.x-grid-cell-Column14 > div");

    WebElement carNumInput;
    WebElement carSearchBtn;


    public boolean searchCarByCarNum (String carNum) throws Exception {
        this.carNumInput = this.waitForElementToAppear(this.carNumTxtExp);
        this.carSearchBtn = this.waitForElementToAppear(this.carSearchBtnExp);

        this.carNumInput.sendKeys(carNum);
        this.carSearchBtn.click();

        Thread.sleep(500);
        List<WebElement> searchCar = this.driver.findElements(this.parkingInCarNumExp);
        if(searchCar.size() > 0) {
            searchCar.get(0).click();
            this.waitForApplyCarNumTab(carNum);
            return true;
        } else {
            return false;
        }
    }
    public void clearCarSearchInput() {
        this.carNumInput.clear();
    }
    public boolean isExitAppliedTicket() {
        List<WebElement> appliedTickets = this.driver.findElements(this.appliedTicketExp);
        if(appliedTickets.size() > 0) {
            return true;
        }
        return false;
    }
    public void waitForApplyCarNumTab(String carNum) {
        By targetCarNumExp = this.targetCarNumExp;
        this.wait.until(new ExpectedCondition<Boolean>() {
            @NullableDecl
            @Override
            public Boolean apply(@NullableDecl WebDriver driver) {
                return carNum.equals(driver.findElement(targetCarNumExp).getText());
            }
        });
    }

    public void applyTicket(String webTicketName) {
        WebElement applyTicketContainer = this.waitForElementToAppear(this.applyTicketContainerExp);
        applyTicketContainer.click();

        List<WebElement> applyTickets = this.waitForElementsToAppear(this.applyTicketsExp);
        for(WebElement ticket: applyTickets) {
            String ticketName = ticket.getText();
            System.out.println(ticketName);
            if(this.isSimilarTicketName(ticketName, webTicketName)) {
                ticket.click();
                this.waitForElementToBeClickAble(this.applyBtnExp).click();
                this.waitForApplyTicket();
                break;
            }
        }
    }

    private void waitForApplyTicket() {
        By appliedTicketExp = this.appliedTicketExp;
        this.wait.until(new ExpectedCondition<Boolean>() {
            @NullableDecl
            @Override
            public Boolean apply(@NullableDecl WebDriver driver) {
                List<WebElement> appliedTickets = driver.findElements(appliedTicketExp);
                if(appliedTickets.size() > 0) {
                    return true;
                }
                return false;
            }
        });
    }


    public boolean isSimilarTicketName(String t1, String t2) {
        if(t1.equals(t2) ||
                t1.indexOf(t2) > 0 ||
                t2.indexOf(t1) > 0) {
            return true;
        }

        return false;
    }
}
