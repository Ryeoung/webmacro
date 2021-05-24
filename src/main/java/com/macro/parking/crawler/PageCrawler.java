package com.macro.parking.crawler;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Setter;

@Setter
public class PageCrawler {
	protected WebDriver driver;
	protected String driverName;
	protected String driverPath;
	public void setupChromeDriver()  {
//	      System.setProperty(driverName, driverPath);
		WebDriver driver = null;

		try {

	      WebDriverManager.chromedriver().setup();

	      ChromeOptions options = new ChromeOptions();
	      options.addArguments("--window-size=1366,768");
//	      options.addArguments("--headless");
	      options.setProxy(null);
	      options.setPageLoadStrategy(PageLoadStrategy.EAGER);
	      options.addArguments("--start-maximized");
	      DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	      driver = new ChromeDriver(capabilities);
		} catch(Exception e) {
			
		}
	      this.driver = driver;
	}
	
	public void quit() {
		this.driver.quit();
	}
	
	public void close() {
		this.driver.close();
	}
	
}
