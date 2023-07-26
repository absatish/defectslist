package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
public class S3Service {

    private final AmazonS3 amazonS3Client;
    private final ObjectMapper objectMapper;

    @Autowired
    public S3Service(final AmazonS3 amazonS3Client1) {
        this.amazonS3Client = amazonS3Client1;
        this.objectMapper = new ObjectMapper();
    }

    public void upload(final String bucketName, final String keyName, final byte[] data) {
        final InputStream inputStream = new ByteArrayInputStream(data);
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");
        amazonS3Client.putObject(bucketName, keyName, inputStream, objectMetadata);
    }

    public URL generatePresignedUrl(final String bucketName, final String key, final Date expires, final HttpMethod method) {
        return amazonS3Client.generatePresignedUrl(bucketName, key, expires, method);
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
