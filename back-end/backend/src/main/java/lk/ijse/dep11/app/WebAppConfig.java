package lk.ijse.dep11.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan
@PropertySource("classpath:/application.properties")
public class WebAppConfig {
    /**
     * Configures a MethodValidationPostProcessor bean.
     * This bean is required for enabling method-level validation using the @Validated annotation.
     *
     * @return The MethodValidationPostProcessor bean.
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }
}
