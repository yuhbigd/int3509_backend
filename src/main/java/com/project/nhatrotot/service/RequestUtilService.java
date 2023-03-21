package com.project.nhatrotot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class RequestUtilService {
        @Autowired
        @Qualifier("keycloak_url_consumer")
        private WebClient webClient;
        @Value("${app.properties.keycloak.CLIENT_SECRET}")
        private String clientSecret;
        @Value("${app.properties.keycloak.client_id}")
        private String clientId;
        @Autowired
        @Qualifier("chat_url_consumer")
        private WebClient chatWebClient;

        public HttpStatus requestCheckOldPassword(String email,
                        String password) {
                MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
                bodyMap.add("client_id", clientId);
                bodyMap.add("username", email);
                bodyMap.add("password", password);
                bodyMap.add("grant_type", "password");
                bodyMap.add("client_secret", clientSecret);
                ResponseEntity<Void> response = webClient.post()
                                .uri("/protocol/openid-connect/token")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .accept(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromFormData(bodyMap))
                                .retrieve().onStatus(
                                                status -> status.value() == 401,
                                                clientResponse -> Mono.empty())
                                .toEntity(Void.class)
                                .block();
                return response.getStatusCode();
        }

        public void changeChatImage(String token, String image, String lastName, String firstName) {
                Map<String, String> bodyMap = new HashMap<>();
                bodyMap.put("image", image);
                bodyMap.put("lastName", lastName);
                bodyMap.put("firstName", firstName);
                chatWebClient.put().uri("/user").headers(h -> h.setBearerAuth(token))
                                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(bodyMap))
                                .retrieve().onStatus(
                                                status -> status.value() == 401,
                                                clientResponse -> Mono.empty())
                                .toEntity(Void.class)
                                .block();

        }
}
