package com.macro.parking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.macro.parking.crawler.WebCrawler;
import com.macro.parking.page.modu.LoginPage;
import com.macro.parking.crawler.ModuPageCrawler;
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
//	@Bean
//	public WebDriverWait webDriverWait(WebDriver driver) throws Exception {
//		return new WebDriverWait(driver, waitTime);
//	}
	@Bean
	public ModuPageCrawler moduPage(LoginPage loginPage, ReservationPage reservationPage) {
		ModuPageCrawler moduPage = new ModuPageCrawler(loginPage, reservationPage);
		moduPage.setId(moduId);
		moduPage.setPassword(moduPw);
		
		return moduPage;
	}
	
	
//    @Bean
//    public WebDriver setupChromeDriver() throws Exception {
//        //System.setProperty(driverName, path);
//        WebDriverManager.chromedriver();
//        
//    	WebDriver driver = null;
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--window-size=1366,768");
//        //options.addArguments("--headless");
//        options.setProxy(null);
//        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//
//        try {
//            /*
//             *
//             * @ params
//             * option : headless
//             *
//             */
//            driver = new ChromeDriver(capabilities);
//        } catch (Exception e) {
//            logger.error("### [driver error] msg: {}, cause: {}", e.getMessage(), e.getCause());
//        }
//
//        return driver;
//    }

}
