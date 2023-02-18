package com.project.nhatrotot.configs.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

/**
 * SwaggerSecurityConfig
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "app.properties.security.enable", havingValue = "true")
@Order(2)
public class SwaggerSecurityConfig {
    @Value("${app.properties.security.swagger_username}")
    private String swaggerUsername;
    @Value("${app.properties.security.swagger_password}")
    private String swaggerPassword;

    @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername(swaggerUsername)
                .password(swaggerPassEncoder().encode(swaggerPassword))
                .roles("SWAGGER_VIEWER").build());
        return manager;
    }

    @Bean
    PasswordEncoder swaggerPassEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/swagger-ui/index.html").antMatcher("/v3/api-docs").authorizeRequests().anyRequest()
                .hasRole("SWAGGER_VIEWER");
        http.httpBasic();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}