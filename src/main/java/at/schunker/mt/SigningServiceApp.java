package at.schunker.mt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SigningServiceApp implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SigningServiceApp.class);
    @Autowired
    private ApplicationContext applicationContext;
    private ConfigurableEnvironment environment;

    /*
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    } */

    public static void main( String[] args ) {
        SpringApplication.run(SigningServiceApp.class, args);
        System.out.println("SigningServiceApp started");
    }

    public SigningServiceApp(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("environment: {}", this.environment.toString());
        List<String> profiles = Arrays.asList("prod", "dev");
        boolean validProfile = false;

        for (String profile : profiles) {
            for (String activeProfile : this.environment.getActiveProfiles()) {
                if (activeProfile.equals(profile)) {
                    validProfile = true;
                }
            }
        }

        if (!validProfile) {
            throw new RuntimeException("invalid profile");
        }
    }
}
