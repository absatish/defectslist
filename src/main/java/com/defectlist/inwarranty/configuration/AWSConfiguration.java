package com.defectlist.inwarranty.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AWSConfiguration {

    @Bean
    @Primary
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    @Autowired
    public AmazonS3 amazonS3Client(final AWSCredentialsProvider amazonAWSCredentials) {
        return AmazonS3Client.builder()
                .withCredentials(amazonAWSCredentials)
                .withRegion("ap-south-1")
                .build();
    }

}
