package com.macro.parking.pageloaded.amino;

import com.macro.parking.pageloaded.PageLoaded;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

/**
 * 아미노 로드 페이지
 */
public class LoginPageLoaded extends PageLoaded {
    public LoginPageLoaded(String expectedTitle, String expectedUrl) {
        super(expectedTitle, expectedUrl);
    }

    /**
     * @param driver 웹 드라이버
     * @return Boolean
     * html 요소드를 살펴 페이지 로드 여부 확인
     */
    @Override
    public Boolean apply(WebDriver driver) {

        int size  = 0 ;
        Boolean isDomAttached = isDomAttachPage(driver);
        if(!isDomAttached) {
            return false;
        }


        try{
            size = driver.findElements(By.tagName("div")).size();

        } catch(StaleElementReferenceException e) {
            return false;
        } catch(Exception e) {
            return false;
        }
        return isDomAttached && (size >= 7);
    }

}
