package com.project.nhatrotot.configs.es;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class EsConfig extends ElasticsearchConfiguration {
    @Value("${app.properties.es.host}")
    private String host;
    @Value("${app.properties.es.username}")
    private String username;
    @Value("${app.properties.es.password}")
    private String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host).withBasicAuth(username, password).withConnectTimeout(1000).build();
    }

}
