package com.macro.parking.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.mchange.v2.c3p0.ComboPooledDataSource;



@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.marco.parking.domain"})
@PropertySource(value = "classpath:db.properties")
public class HibernateConfig {

	  @Autowired
	  private Environment env;
	  
	  
	  @Bean
	  public LocalSessionFactoryBean sessionFactory() {
	    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
	    
	    sessionFactoryBean.setDataSource(dataSource());
	    System.out.println("!!!!!!!!!!!!!!!");
	    sessionFactoryBean.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
	    
	    Properties properties = new Properties();
	    properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
	    properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
	    
	    sessionFactoryBean.setHibernateProperties(properties);
	    
	    return sessionFactoryBean;
	  }
	  
	  @Bean
	  public DataSource dataSource() {
	    ComboPooledDataSource dataSource = new ComboPooledDataSource();
	    try {
	      dataSource.setDriverClass(env.getProperty("jdbc.driver"));
	    } catch (PropertyVetoException e) {
	      throw new RuntimeException(e);
	    }
	    dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
	    dataSource.setUser(env.getProperty("jdbc.username"));
	    dataSource.setPassword(env.getProperty("jdbc.password"));
	    dataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("conneciton.pool.initialPoolSize")));
	    dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
	    dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));
	    dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));
	        
	    return dataSource;
	  }
	  
	  @Bean
	  public HibernateTransactionManager transactionManager() {
	    HibernateTransactionManager txManager = new HibernateTransactionManager();
	    txManager.setSessionFactory(sessionFactory().getObject());
	    return txManager;
	  }
}