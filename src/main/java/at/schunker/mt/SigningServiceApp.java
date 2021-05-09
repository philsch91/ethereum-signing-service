package at.schunker.mt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SigningServiceApp {
    @Autowired
    private ApplicationContext applicationContext;

    /*
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    } */

    public static void main( String[] args ) {
        SpringApplication.run(SigningServiceApp.class, args);
        System.out.println( "SigningServiceApp started" );
    }
}
