package ru.bsuedu.cad.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Import(ConfigBasic.class)
@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.app")
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.app.repository")
@EnableTransactionManagement
public class ConfigJpa {
    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.bsuedu.cad.app.entity"); // Escanea entidades
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true); // Muestra sentencias SQL en logs
        vendorAdapter.setGenerateDdl(true); // Habilita generación de DDL
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect"); // Dialecto MySQL
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true"); // Muestra SQL
        properties.put("hibernate.format_sql", "true"); // Formato legible
        properties.put("hibernate.hbm2ddl.auto", "update"); // Crea/actualiza tablas
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}