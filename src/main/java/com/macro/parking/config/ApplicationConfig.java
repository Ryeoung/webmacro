package com.macro.parking.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.macro.parking.service","com.macro.parking.dao" })
@Import({HibernateConfig.class,SeleniumConfig.class})
public class ApplicationConfig {

}
