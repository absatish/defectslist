package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final AmazonS3 amazonS3Client;
    private final ObjectMapper objectMapper;

    @Autowired
    public S3Service(final AmazonS3 amazonS3Client1) {
        this.amazonS3Client = amazonS3Client1;
        this.objectMapper = new ObjectMapper();
    }

    public void upload(final String bucketName, final String keyName, final byte[] data, final String contentType) {
        final InputStream inputStream = new ByteArrayInputStream(data);
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        amazonS3Client.putObject(bucketName, keyName, inputStream, objectMetadata);
    }

    public URL generatePresignedUrl(final String bucketName, final String key, final Date expires, final HttpMethod method) {
        return amazonS3Client.generatePresignedUrl(bucketName, key, expires, method);
    }

    public String getGoodParts() {
        final S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest("captcha-bucket", "items.html"));
        final S3ObjectInputStream s3data = s3Object.getObjectContent();
        final InputStreamReader streamReader = new InputStreamReader(s3data, StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(streamReader);
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public String getGoodPartsModified() {
        final S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest("captcha-bucket", "items.html"));
        return LocalDateTime.ofInstant(s3Object.getObjectMetadata().getLastModified().toInstant(), ZoneId.of("Asia/Kolkata")).toString();
    }

    public Map<String, String> getCredentials() {
        final S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest("bucket-tracker", "secrets.json"));
        try {
            final byte[] s3data = s3Object.getObjectContent().readAllBytes();
            return objectMapper.readValue(s3data, new TypeReference<>() {
            });
        } catch (final IOException ioException) {
            System.out.println("hi");
            return Collections.emptyMap();
        }
    }


}
