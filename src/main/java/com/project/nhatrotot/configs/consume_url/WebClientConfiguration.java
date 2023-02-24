package com.project.nhatrotot.configs.consume_url;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebClientConfiguration {
    @Value("${app.properties.keycloak.token_url}")
    private String KEYCLOAK_TOKEN_URL;
    public static final int TIMEOUT = 1000;

    private static TcpClient createTcpClient() {
        return TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS));
                });
    }

    @Bean("keycloak_url_consumer")
    WebClient webClientWithTimeout() {
        return WebClient.builder()
                .baseUrl(KEYCLOAK_TOKEN_URL)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(createTcpClient())))
                .build();
    }
}