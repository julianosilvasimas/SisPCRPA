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
    entityManagerFactoryRef = "csrEntityManagerFactory",
    transactionManagerRef = "csrTransactionManager",
    basePackages = "com.prolagos.SispcRPA.db.csr.repository")
public class CsrDataSourceConfig {

	 @Primary
	    @Bean(name = "csrDataSource")
	    @ConfigurationProperties(prefix = "csr.datasource")
	    public HikariDataSource csrDataSource() {
	        return DataSourceBuilder.create()
	                                .type(HikariDataSource.class)
	                                .build();
	    }
	    @Primary
	    @Bean(name = "csrEntityManagerFactory")
	    public LocalContainerEntityManagerFactoryBean csrEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("csrDataSource") DataSource dataSource) {
	    	
	        return builder.dataSource(dataSource)
	                      .packages("com.prolagos.SispcRPA.db.csr.model")
	                      .persistenceUnit("csrPU")
	                      .build();
	    }
	    @Primary
	    @Bean(name = "csrTransactionManager")
	    public PlatformTransactionManager csrTransactionManager(@Qualifier("csrEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
	        return new JpaTransactionManager(entityManagerFactory);
	    }
	    @Bean
	    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	        return new PersistenceExceptionTranslationPostProcessor();
	    }
	     
	    Properties additionalProperties() {
	        Properties properties = new Properties();
	        properties.setProperty("hibernate.hbm2ddl.auto", "update");
	        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
	        properties.setProperty("hibernate.use-new-id-generator-mappings", "false");
	        return properties;
	    }
	
}
