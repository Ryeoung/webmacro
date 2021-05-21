package com.macro.parking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.page.ipark.CarSearchPage;
import com.macro.parking.page.ipark.IParkLoginPage;
import com.macro.parking.page.ipark.MainPage;
import com.macro.parking.page.ipark.TicketApplyPage;
import com.macro.parking.page.modu.ModuLoginPage;
import com.macro.parking.crawler.IParkingPageCralwer;
import com.macro.parking.crawler.ModuPageCrawler;
import com.macro.parking.crawler.PageCrawler;
import com.macro.parking.page.modu.ReservationPage;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@PropertySource("classpath:application.properties")
public class SeleniumConfig {
    private static final Logger logger = LoggerFactory.getLogger(SeleniumConfig.class);
    @Value("${ipark.url}")
	private String iParkUrl;

	@Value("${modu.id}")
	private String moduId;

	@Value("${modu.password}")
	private String moduPw;

	@Value("${modu.url}")
	private String moduUrl;

	@Value("${web.driver.name}")
	private String driverName;
	
	@Value("${web.driver.path}")
	private String path;
	
	@Value("${web.explicit.wait}")
	private int waitTime;
	

	@Bean
	public WebCrawler getWebCrawler() throws Exception {
		WebCrawler webCrawler = new WebCrawler();
		//webCrawler.setDriver(driver);
		webCrawler.setDriverName(driverName);
		webCrawler.setPath(path);
		//webCrawler.setWait(wait);
		webCrawler.setIParkUrl(iParkUrl);
		webCrawler.setModuId(moduId);
		webCrawler.setModuPw(moduPw);
		webCrawler.setModuUrl(moduUrl);
		webCrawler.setDriverName(driverName);
		webCrawler.setPath(path);
		return webCrawler;
		
	}

	@Bean
	public ModuPageCrawler moduPage(ModuLoginPage moduLoginPage, ReservationPage reservationPage) {
		ModuPageCrawler moduPage = new ModuPageCrawler(moduLoginPage, reservationPage);
		moduPage.setId(moduId);
		moduPage.setPassword(moduPw);
		setDriverInfo(moduPage);
		return moduPage;
	}
	
	@Bean
	public IParkingPageCralwer iparkPage(IParkLoginPage iParkLoginPage, MainPage mainPage, 
											CarSearchPage carSearchPage, TicketApplyPage ticketApplyPage) {
		IParkingPageCralwer iParkinPage = new IParkingPageCralwer(iParkLoginPage, mainPage, carSearchPage, ticketApplyPage);
		setDriverInfo(iParkinPage);

		return iParkinPage;
	}
	
	public void setDriverInfo(PageCrawler pageCrawler) {
		pageCrawler.setDriverName(this.driverName);
		pageCrawler.setDriverPath(this.path);
	}

}
