package com.example.prozet.modules.file.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.utils.UtilsClass;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3 amazonS3;

    public String uploadS3File(MultipartFile multipartFile, FileType category) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String contentType = getExtension(fileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket,
                category.bucketName() + createFileName(fileName),
                multipartFile.getInputStream(),
                metadata);

        amazonS3.putObject(putObjectRequest);
        return amazonS3.getUrl(putObjectRequest.getBucketName(), putObjectRequest.getKey()).toString();

    }

    public void deleteS3File(String fileKey) throws AmazonServiceException {
        amazonS3.deleteObject(bucket, fileKey);
    }

    public String getExtension(String fileName) {

        String ext = fileName.split("\\.")[1];
        String contentType = "";

        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "jpg":
                contentType = "image/jpg";
                break;
        }

        return contentType;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().substring(0, 4).concat(UtilsClass.getCurrentDate()).concat("-")
                .concat(fileName);
    }

}
