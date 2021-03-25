package com.macro.parking.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
@EnableTransactionManagement
public class HibernateConfig {

	@Autowired
	private ApplicationContext context;

	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setPackagesToScan("classpath:com.macro.parking.dto");
		factoryBean.setConfigLocation(context.getResource("classpath:hibernateConfig.xml"));
		return factoryBean;
	}

	@Bean
	public HibernateTransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory.getObject());
		return transactionManager;
	}
}