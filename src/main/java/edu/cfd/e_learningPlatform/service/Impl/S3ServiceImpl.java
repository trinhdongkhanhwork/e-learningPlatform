package edu.cfd.e_learningPlatform.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3ServiceImpl implements edu.cfd.e_learningPlatform.service.S3Service {

    private final AmazonS3 amazonS3;

    @Value("${s3.bucketName}")
    private String BUCKET_NAME;

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        PutObjectRequest putRequest = new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream(), new ObjectMetadata());
        amazonS3.putObject(putRequest);
        return "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + fileName;
    }

}
