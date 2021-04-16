package com.macro.parking.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.mchange.v2.c3p0.ComboPooledDataSource;



@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.marco.parking.domain"})
@EnableJpaRepositories(basePackages = "com.macro.parking.dao")
@PropertySource(value = "classpath:db.properties")
public class HibernateConfig {

	  @Autowired
	  private Environment env;
	  
	  
	  @Bean
	  public DataSource dataSource() {
		  	BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
			dataSource.setUrl(env.getProperty("jdbc.url"));
			dataSource.setUsername(env.getProperty("jdbc.user"));
			dataSource.setPassword(env.getProperty("jdbc.pass"));
			return dataSource;
	}

	  
	    @Bean(name = "entityManagerFactory")
	    public LocalContainerEntityManagerFactoryBean  entityManagerFactory() {
//		  HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		    vendorAdapter.setGenerateDdl(true);
		  
	    //LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    System.out.println("1");
	    factory.setDataSource(dataSource());
	    System.out.println("2");
	    factory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
	    System.out.println("3");
	    factory.setJpaVendorAdapter(jpaVendorAdapter());
	    System.out.println("4");
	   
	   
	    factory.setJpaProperties(hibernateProperties());
	    System.out.println("5");
	    //	   sessionFactoryBean.setHibernateProperties(properties);
	    
	    return factory;
	  }
	  
	
  
	  @Bean
	  public JpaVendorAdapter jpaVendorAdapter(){
	      HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
	      //adapter.setDatabase();
//	      adapter.setShowSql(true);
//	      adapter.setGenerateDdl(false);
//	      adapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
//	      adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
	      return adapter;
	  }
	  
	  @Bean(name = "transactionManager")
	  public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
	    txManager.setEntityManagerFactory(entityManagerFactory().getObject());
	    return txManager;
	  }
	   private Properties hibernateProperties() {
	        Properties properties = new Properties();
	        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		    properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		    properties.setProperty("hibernate.hbm2ddl.auto", "update");
//		    properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
//	        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
	        return properties;
	    }
}