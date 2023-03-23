package com.project.nhatrotot.configs.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@EnableWebSecurity
@ConditionalOnProperty(name = "app.properties.security.enable", havingValue = "true")
@Configuration
@Order(3)
@EnableMethodSecurity(prePostEnabled = true)
public class AllowAnonymousConfig {
    @Bean
    SecurityFilterChain anonymousFilterChain(HttpSecurity http)
            throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtAuthorityConverter());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();

        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        });
        http.requestMatchers().antMatchers("/api/houses").and()
                .requestMatchers().antMatchers("/api/houses/*")
                .and().authorizeHttpRequests().anyRequest().permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter);
        return http.build();
    }
}