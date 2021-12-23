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

/**
 * 아미노 주차권 발권 페이지 객체
 */
@Component("aminoTicketApplyPage")
public class TicketApplyPage extends BasePage {
    By modalConfirmOkBtnBy = By.className("modal-btn");
    By searchedCarNumBy = By.className("cellselected");
    By ticketsBy = By.cssSelector("a[name='btnDscntType']");
    By appliedTicketBy = By.cssSelector("#gridDtl tr.ev_dhx_skyblue > td:nth-child(2)");

    @FindBy(id = "schCarNo")
    WebElement carInputElmt;

    @FindBy(className = "btnS1_1")
    WebElement carSearchBtnElmt;

    List<WebElement> ticketElmts;

    /**
     * 페이지 로드
     */
    public void load() {
        this.waitForElementToAppear(this.modalConfirmOkBtnBy).click();
    }

    /**
     * @param carNum 차량 넘버
     *   차량 넘버 검색
     */
    public void searchCar(String carNum) {
        this.carInputElmt.clear();
        this.carInputElmt.sendKeys(carNum);
        this.carSearchBtnElmt.click();
    }

    /**
     * @param carNum 차량 넘버
     * @return boolean
     *
     *  차량이 존재하는 지 확인
     */
    public boolean isExistCar(String carNum) {
        List<WebElement> searchedCar = this.driver.findElements(searchedCarNumBy);
        if(searchedCar.size() > 0) {
            //있는 경우
            return true;
        }

        this.waitForElementToAppear(this.modalConfirmOkBtnBy).click();
        return false;
    }

    /**
     * @param ticketName 주차권 이름
     * @return boolean
     *
     *  해당 주차권이 이미 존재하는 지 확인
     */
    public boolean isHavingTicket(String ticketName) {
        //td[title='ticketName']
        List<WebElement> appliedTicket = this.driver.findElements(this.appliedTicketBy);
        if(appliedTicket.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * @param applyTicketName 발권할 주차권 이름
     * @return boolean
     *  주차권 발차
     */
    public boolean applyTicket(String applyTicketName) {
        this.ticketElmts = this.waitForElementsToAppear(this.ticketsBy);

        for(WebElement ticketElmt: this.ticketElmts) {
            String ticketName = ticketElmt.getText();
            if(ticketName.equals(applyTicketName) ||
                    ticketName.indexOf(applyTicketName) >= 0 ||
                    applyTicketName.indexOf(ticketName) >= 0){
                ticketElmt.click();
                this.waitForElementToAppear(this.modalConfirmOkBtnBy).click();
                this.waitForApplyTicket(applyTicketName);
                return true;
            }
        }

        return false;
    }

    /**
     * @param ticketName 주차권 이름
     *    주차권이 들어갔는 지 확인
     */
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
