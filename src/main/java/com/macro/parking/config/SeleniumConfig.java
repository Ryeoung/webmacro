package com.macro.parking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.macro.parking.crawler.WebCrawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
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

	@Value("${modu.pw}")
	private String moduPw;

	@Value("${modu.url}")
	private String moduUrl;

	@Value("${web.driver.name}")
	private String driverName;
	
	@Value("${web.driver.path}")
	private String path;
	
    private WebDriver driver;

	@Bean
	public WebCrawler getWebCrawler() {
		WebCrawler webCrawler = new WebCrawler();

		webCrawler.setIParkUrl(iParkUrl);
		webCrawler.setModuId(moduId);
		webCrawler.setModuPw(moduPw);
		webCrawler.setModuUrl(moduUrl);
		return webCrawler;
		
	}
	
	@Bean
    public WebDriver getDriver() {
        return driver;
    }

    @Bean
    public WebDriver setupChromeDriver() throws Exception {
        System.setProperty(driverName, path);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1366,768");
        options.addArguments("--headless");
        options.setProxy(null);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability("pageLoadStrategy", "none");

        try {
            /*
             *
             * @ params
             * option : headless
             *
             */
            driver = new ChromeDriver(capabilities);
        } catch (Exception e) {
            logger.error("### [driver error] msg: {}, cause: {}", e.getMessage(), e.getCause());
        }

        return driver;
    }

}
