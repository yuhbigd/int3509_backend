package com.project.nhatrotot.configs.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakAdminConfig {
    @Value("${app.properties.keycloak.CLIENT_SECRET}")
    private String clientSecret;
    @Value("${app.properties.keycloak.ADMIN_USERNAME}")
    private String adminUsername;
    @Value("${app.properties.keycloak.ADMIN_PASSWORD}")
    private String adminPassword;

    @Bean
    protected Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.PASSWORD).realm("nhatrotot").clientId("backend")
                .clientSecret(clientSecret).username(adminUsername).password(adminPassword)
                .serverUrl("http://localhost:8099").build();
    }
}