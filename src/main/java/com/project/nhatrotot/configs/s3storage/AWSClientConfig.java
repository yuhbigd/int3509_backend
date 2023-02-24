package com.project.nhatrotot.configs.s3storage;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Configuration
public class AWSClientConfig {
    @Value("${app.properties.aws.access_id}")
    private String accessKeyId;
    @Value("${app.properties.aws.secret_key}")
    private String secretAccessKey;

    private AmazonS3 createClient() {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    @Bean
    AmazonS3 client() {
        return createClient();
    }

    @Bean
    TransferManager transferManager() {

        return TransferManagerBuilder.standard()
                .withS3Client(createClient())
                .withMultipartUploadThreshold((long) (5 * 1024 * 1024))
                .withExecutorFactory(() -> Executors.newFixedThreadPool(10))
                .build();
    }
}
