package com.macro.parking.page;

import java.util.List;

import javax.annotation.PostConstruct;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.macro.parking.pageloaded.PageLoaded;


public class BasePage{
    private static final int TIMEOUT = 100;
    private static final int POLLING = 100;

    protected WebDriver driver;
    
    protected WebDriverWait wait;

    public void  init(WebDriver driver) {
    	this.driver = driver;
        this.init();
    }
    public void  init() {
        wait = new WebDriverWait(this.driver, TIMEOUT, POLLING);
        PageFactory.initElements(new AjaxElementLocatorFactory(this.driver, TIMEOUT), this);
    }
    
    protected List<WebElement> waitForElementsToAppear(By locator) {
    	return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected WebElement waitForElementToAppear(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected Boolean waitForElementToDisappear(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected Boolean waitForTextToDisappear(By locator, String text) {
        return wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(locator, text)));
    }
    
    protected WebElement waitForElementToBeClickAble(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForElementToBeClickAble(WebElement webElement) {
        return wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    protected void waitForPageLoad(PageLoaded pagedLoaded){
    	wait.until(pagedLoaded);
    }
    

    public void navigate(String url) {
        this.driver.navigate().to(url);
    }
}
