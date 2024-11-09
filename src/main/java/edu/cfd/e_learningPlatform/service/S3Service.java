package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    List<Video> uploadFiles(List<MultipartFile> files) throws IOException;
    String uploadImage(MultipartFile file) throws IOException;
    }
