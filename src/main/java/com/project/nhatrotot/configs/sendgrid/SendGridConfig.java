package com.project.nhatrotot.configs.sendgrid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {
    @Value("${app.properties.sendgrid.api_key}")
    private String accessKeyId;
    @Value("${app.properties.sendgrid.template_id}")
    private String templateId;

    @Bean
    public SendGridUlt createSendGridUlt() {
        return new SendGridUlt(accessKeyId, templateId);
    }
}
