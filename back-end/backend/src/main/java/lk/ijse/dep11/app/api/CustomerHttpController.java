package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.core.env.Environment;





public class CustomerHttpController {

    private final HikariDataSource pool;

    public CustomerHttpController(Environment env) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        config.setUsername(env.getRequiredProperty("spring.datasource.username"));
        config.setPassword(env.getRequiredProperty("spring.datasource.password"));
        config.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        config.setMaximumPoolSize(env.getRequiredProperty("spring.datasource.hikari.maximum-pool-size", Integer.class));
        pool = new HikariDataSource(config);
    }


}