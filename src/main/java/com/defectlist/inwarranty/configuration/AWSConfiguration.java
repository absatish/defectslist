package com.defectlist.inwarranty.configuration;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AWSConfiguration {

    @Bean
    @Primary
    public AWSStaticCredentialsProvider awsCredentialsProvider(@Value("${variables.button}") final String key,
    @Value("${variables.link}") final String value) {
        final String k = resolveKey(key);
        final String v = resolveValue(value);
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(k, v));
    }

    @Bean
    @Autowired
    public AmazonS3 amazonS3Client(final AWSStaticCredentialsProvider amazonAWSCredentials) {
        return AmazonS3Client.builder()
                .withCredentials(amazonAWSCredentials)
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }

    @Bean
    @Autowired
    public AmazonSimpleEmailService amazonSimpleEmailService(final AWSStaticCredentialsProvider amazonAWSCredentials) {
                return AmazonSimpleEmailServiceClientBuilder.standard()
                        .withCredentials(amazonAWSCredentials)
                        .withRegion(Regions.AP_SOUTH_1)
                        .build();
    }

    private String resolveKey(final String key) {
        final String newKey = key.replaceAll("X", "")
                .replaceAll("Y", "")
                .replaceAll("Z", "")
                .replaceAll("Q", "")
                .replaceAll("L", "")
                .replaceAll("1", "")
                .replaceAll("5", "")
                .replaceAll("6", "");
        String k = "";
        for(int i = newKey.length() - 1; i>0; i = i - 2) {
            k = k.concat(newKey.substring(i-1, i));
        }
        return k;
    }

    private String resolveValue(final String value) {
        final String newValue = value.replaceAll("-", "")
                .replaceAll("@", "")
                .replaceAll(".com", "")
                .replaceAll("https://", "")
                .replaceAll("_", "");
        String v = "";
        for(int i = newValue.length(); i>0; i = i - 1) {
            v = v.concat(newValue.substring(i-1, i));
        }
        return v;
    }



}
