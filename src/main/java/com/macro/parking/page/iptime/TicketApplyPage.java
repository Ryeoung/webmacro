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

/**
 * 주차권 발권 페이지
 */
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


    /**
     * @param carNum 차람 넘버
     * @return
     * @throws Exception
     *
     *  차량 입차 여부 확인
     */
    public boolean isExistCar(String carNum) throws Exception {
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

    /**
     * 차량 번호 지우기
     */
    public void clearCarSearchInput() {
        this.carNumInput.clear();
    }

    /**
     * @return boolean
     *  티켓이 존재하는 지 확인
     */
    public boolean isHavingTicket() {
        List<WebElement> appliedTickets = this.driver.findElements(this.appliedTicketExp);
        if(appliedTickets.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @param carNum 차량 넘버
     *   차량 넘버거 페이지에 로드될 때까지 확인
     */
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

    /**
     * @param applyTicketName
     *  주차권 발권
     */
    public void applyTicket(String applyTicketName) {
        WebElement applyTicketContainer = this.waitForElementToAppear(this.applyTicketContainerExp);
        applyTicketContainer.click();

        List<WebElement> applyTickets = this.waitForElementsToAppear(this.applyTicketsExp);
        for(WebElement ticket: applyTickets) {
            String ticketName = ticket.getText();
            if(this.isSimilarTicketName(ticketName, applyTicketName)) {
                ticket.click();
                this.waitForElementToBeClickAble(this.applyBtnExp).click();
                this.waitForApplyTicket();
                break;
            }
        }
    }

    /**
     * 주차권 발권까지 기다리기
     */
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


    /**
     * @param t1 티켓1
     * @param t2 티켓 2
     * @return boolean
     *  티켓 이름이 같거나 둘 중 하나가 다른 하나의 일부분일 경우
     */
    public boolean isSimilarTicketName(String t1, String t2) {
        if(t1.equals(t2) ||
                t1.indexOf(t2) > 0 ||
                t2.indexOf(t1) > 0) {
            return true;
        }

        return false;
    }
}
