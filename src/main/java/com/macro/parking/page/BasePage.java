package com.macro.parking.page;

import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.macro.parking.pageloaded.PageLoaded;


/**
 * 페이지 객체
 */
public class BasePage{
    private static final int TIMEOUT = 100;
    private static final int POLLING = 100;

    protected WebDriver driver;
    
    protected WebDriverWait wait;
    
    protected JavascriptExecutor javascriptExcutor;

    /**
     * @param driver 웹 드라이버
     * 페이지 객체 초기화
     */
    public void  init(WebDriver driver) {
    	this.driver = driver;
        this.init();
    }

    /**
     * PageFactory에 페이지 로드
     */
    public void  init() {
        wait = new WebDriverWait(this.driver, TIMEOUT, POLLING);
        javascriptExcutor = (JavascriptExecutor) this.driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(this.driver, TIMEOUT), this);
    }

    /**
     * @param locator html path
     * @return List<WebElement>
     *  html 요소들 뜰 때까지 wait
     */
    protected List<WebElement> waitForElementsToAppear(By locator) {
    	return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * @param locator html path
     * @return WebElement
     *
     *  html 요소가 뜰 때까지 wait
     */
    protected WebElement waitForElementToAppear(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    /**
     * @param locator html path
     * @param text 텍스트
     * @return Boolean
     *  텍스트가 없어질 때까지 wait
     */
    protected Boolean waitForTextToDisappear(By locator, String text) {
        return wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(locator, text)));
    }

    /**
     * @param locator html path
     * @return WebElement
     *  button click 이벤트 가능할 때까지 wait
     */
    protected WebElement waitForElementToBeClickAble(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * @param webElement html element
     * @return WebElement
     *  button click 이벤트 가능할 때까지 wait
     */
    protected WebElement waitForElementToBeClickAble(WebElement webElement) {
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    /**
     * @param pagedLoaded 페이지 로드 객체
     *  페이지 로드까지 기다리기
     */
    protected void waitForPageLoad(PageLoaded pagedLoaded){
    	wait.until(pagedLoaded);
    }


    /**
     * @param url
     * url로 페이지 로드
     */
    public void navigate(String url) {
        this.driver.navigate().to(url);
    }
}
