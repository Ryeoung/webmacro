package com.macro.parking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HibernateConfig.class, DBConfig.class, WebMvcContextConfig.class, SeleniumConfig.class})
public class ApplicationConfig {

}
