package com.macro.parking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.macro.parking.crawler.WebCrawler;

@Configuration
@ComponentScan(basePackages = { "com.marco.parking.dto", "com.marco.parking.domain"})
@PropertySource("classpath:application.properties")
public class SeleniumConfig {
	@Value("${ipark.url}")
	private String iParkUrl;

	@Value("${modu.id}")
	private String moduId;

	@Value("${modu.pw}")
	private String moduPw;

	@Value("${modu.url}")
	private String moduUrl;
	
	@Value("${web.driver.name}")
	private String driver;
	
	@Value("${web.driver.path}")
	private String path;
	
	
	@Bean
	public WebCrawler webCrawler() {
		WebCrawler webCrawler = new WebCrawler();
		webCrawler.setWebDriverId(driver);
		webCrawler.setWebDriverPath(path);

		webCrawler.setIParkUrl(iParkUrl);
		webCrawler.setModuId(moduId);
		webCrawler.setModuPw(moduPw);
		webCrawler.setModuUrl(moduUrl);
		return webCrawler;
	}
}
