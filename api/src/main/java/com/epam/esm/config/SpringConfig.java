package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@EnableTransactionManagement
@PropertySources({@PropertySource("classpath:application-${spring.profiles.active}.properties")})
public class SpringConfig implements TransactionManagementConfigurer {

    private final static int DEFAULT_INITIAL_SIZE = 3;
    private final static String DATABASE_DRIVER = "ds.database-driver";
    private final static String DATABASE_URL = "ds.url";
    private final static String DATABASE_USERNAME = "ds.username";
    private final static String DATABASE_PASSWORD = "ds.password";
    private final static String DATABASE_INIT_SIZE = "db.initial-size";

    private final ApplicationContext applicationContext;

    @Autowired
    private Environment env;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty(DATABASE_DRIVER));
        dataSource.setUrl(env.getProperty(DATABASE_URL));
        dataSource.setUsername(env.getProperty(DATABASE_USERNAME));
        dataSource.setPassword(env.getProperty(DATABASE_PASSWORD));
        String initSizeProperty = env.getProperty(DATABASE_INIT_SIZE);
        dataSource.setInitialSize(initSizeProperty != null ? Integer.valueOf(initSizeProperty) : DEFAULT_INITIAL_SIZE);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

}
