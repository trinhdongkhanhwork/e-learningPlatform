package edu.cfd.e_learningPlatform.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    String uploadImage(MultipartFile file) throws IOException;
    }
