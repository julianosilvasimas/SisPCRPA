package com.prolagos.SispcRPA.config.db;



import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "sispcEntityManagerFactory",
    transactionManagerRef = "sispcTransactionManager",
    basePackages = "com.prolagos.sispcRPA.db.sispc.repository")
public class SispcDataSourceConfig {
	
	    @Primary
	    @Bean(name = "sispcDataSource")
	    @ConfigurationProperties(prefix = "sispc.datasource")
	    public HikariDataSource sispcDataSource() {
	        return DataSourceBuilder.create()
	                                .type(HikariDataSource.class)
	                                .build();
	    }
	    @Primary
	    @Bean(name = "sispcEntityManagerFactory")
	    public LocalContainerEntityManagerFactoryBean sispcEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("sispcDataSource") DataSource dataSource) {
	    	
	        return builder.dataSource(dataSource)
	                      .packages("com.prolagos.sispcRPA.db.sispc.model")
	                      .persistenceUnit("sispcPU")
	                      .build();
	    }
	    @Primary
	    @Bean(name = "sispcTransactionManager")
	    public PlatformTransactionManager sispcTransactionManager(@Qualifier("sispcEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
	        return new JpaTransactionManager(entityManagerFactory);
	    }
	    @Bean
	    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	        return new PersistenceExceptionTranslationPostProcessor();
	    }
	     
	    Properties additionalProperties() {
	        Properties properties = new Properties();
	        properties.setProperty("hibernate.hbm2ddl.auto", "update");
	        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
	        return properties;
	    }

}
