package com.project.nhatrotot.configs.security;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@ConditionalOnProperty(name = "app.properties.security.enable", havingValue = "true")
@Configuration
@Order(3)
public class NoAuthSecurityConfig {
    @Bean
    SecurityFilterChain NoAuthFilterChain(HttpSecurity http)
            throws Exception {
        http.cors().configurationSource(corsConfigurationSource());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.requestMatchers().antMatchers("/api/auth/signup").and()
                .requestMatchers().antMatchers("/api/auth/reset")
                .and()
                .requestMatchers().antMatchers("/api/users").and()
                .requestMatchers().antMatchers("/api/users/*").and()
                .requestMatchers().antMatchers("/api/payment/vnpay-return").and()
                .authorizeHttpRequests().anyRequest()
                .permitAll();
        return http.build();
    }

    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/auth/*", configuration);
        source.registerCorsConfiguration("/api/users", configuration);
        source.registerCorsConfiguration("/api/users/*", configuration);
        source.registerCorsConfiguration("/api/payment/vnpay-return", configuration);

        return source;
    }
}
