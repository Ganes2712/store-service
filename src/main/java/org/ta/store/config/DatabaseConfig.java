package org.ta.store.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DatabaseConfig {


    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String name;
    @Value("${spring.datasource.password}")
    private String pwd;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private String connTimeout;
    @Value("${spring.datasource.hikari.idle-timeout}")
    private String idleTimeout;
    @Value("${spring.datasource.hikari.max-lifetime}")
    private String maxLifetime;
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private String maxPool;
    @Value("${spring.datasource.hikari.minimum-idle}")
    private String minIdle;
    @Value("${spring.datasource.hikari.pool-name}")
    private String poolName;

    @Bean
    @Primary
    public DataSource getDataSource()
    {
        Properties prop = new Properties();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(name);
        dataSource.setPassword(pwd);

        prop.put("spring.datasource.hikari.connection-timeout",connTimeout);
        prop.put("spring.datasource.hikari.idle-timeout",idleTimeout);
        prop.put("spring.datasource.hikari.max-lifetime",maxLifetime);
        prop.put("spring.datasource.hikari.maximum-pool-size",maxPool);
        prop.put("spring.datasource.hikari.minimum-idle",minIdle);
        prop.put("spring.datasource.hikari.pool-name",poolName);


        dataSource.setDataSourceProperties(prop);

        return dataSource;
    }
}
