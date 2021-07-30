package com.macro.parking.crawler;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Setter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.List;

@Setter
public class PageCrawler {
	protected WebDriver driver;
	protected String driverName;
	protected String driverPath;
	protected final String HOST_URL = "http://selenium-hub:4444/wd/hub";
	public void setupChromeDriver()  {
		WebDriver driver = null;

		try {
			WebDriverManager.chromedriver().setup();
	  		ChromeOptions options = new ChromeOptions();
	  		options.addArguments("--window-size=1920,1080");
	  		//도커 컨테이너에서 크롬 드라이브를 사용 시 /dev/shm 메모리가 부족해서 사용하지 않는 다고 표시
//			options.addArguments("--no-sandbox");
//			options.addArguments("--disable-dev-shm-usage");

//	  		options.addArguments("--headless");
//	  		options.addArguments("--disable-gpu"); // gpu 가속 끄
//			options.addArguments("User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4044.122 Safari/537.36");

			options.setProxy(null);
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);


			driver = new ChromeDriver(options);
//			driver = new RemoteWebDriver(new URL(HOST_URL), options);
		} catch(Exception e) {
			e.printStackTrace();
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
