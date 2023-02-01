package com.example.demo.websecurity;

import com.example.demo.Validator.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true,proxyTargetClass=true)
public class SpringSecurityConfiguration {
    @Bean
    public UserValidator userValidator(){
        return new UserValidator();
    }
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http.csrf()
                    .disable()
                    .authorizeRequests()
                    .requestMatchers("/v1/user/*")
                    .authenticated()
                    .anyRequest()
                    .permitAll()
                    .and()
                    .httpBasic();
            return http.build();
        } catch (Exception e) {
            System.out.println("Error msg:::: " + e);
        }
        return null;
    }
}
