package com.example.prozet.module.file.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.prozet.config.AwsConfigTest;

import static org.assertj.core.api.Assertions.assertThat;

import io.findify.s3mock.S3Mock;

@Import(AwsConfigTest.class)
@SpringBootTest
@ActiveProfiles("test")
public class S3ServiceTest {

    private static String bucket = "bucket";

    @Autowired
    private AmazonS3 amazonS3;

    @BeforeAll
    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        s3Mock.start();
        amazonS3.createBucket(bucket);
    }

    @AfterAll
    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        amazonS3.shutdown();
        s3Mock.stop();
    }

    @Test
    @DisplayName("s3 파일업로드 테스트")
    public void fileS3Upload() throws IOException {
        // given
        String path = "src/test/resources/rocket-launcher.png";
        String fileName = "rocket-launcher";
        String contentType = "image/png";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket,
                "test/" + path,
                new ByteArrayInputStream("".getBytes()),
                objectMetadata);
        amazonS3.putObject(putObjectRequest);

        // when
        S3Object s3Object = amazonS3.getObject(bucket, putObjectRequest.getKey());
        // then
        assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(contentType);

    }

}
