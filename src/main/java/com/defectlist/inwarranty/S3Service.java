package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.UploadObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class S3Service {

    private final AmazonS3 amazonS3Client;

    @Autowired
    public S3Service(final AmazonS3 amazonS3Client1) {
        this.amazonS3Client = amazonS3Client1;
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
}
