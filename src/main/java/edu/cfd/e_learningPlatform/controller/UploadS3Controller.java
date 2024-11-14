package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.dto.response.UploadImageResponse;
import edu.cfd.e_learningPlatform.entity.Video;
import edu.cfd.e_learningPlatform.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3/upload")
public class UploadS3Controller {
    private final S3Service s3Service;

    public UploadS3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/image")
    public ApiResponse<UploadImageResponse> uploadImages(@RequestParam("img") MultipartFile image) throws IOException {
        String urlImg = s3Service.uploadImage(image);
        UploadImageResponse result = UploadImageResponse.builder()
                .urlImg(urlImg)
                .build();

        return ApiResponse.<UploadImageResponse>builder()
                .result(result)
                .message("Image uploaded successfully")
                .build();
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping("/video")
    public ResponseEntity<List<Video>> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<Video> videos = s3Service.uploadFiles(files);
        return ResponseEntity.ok(videos);
    }
}
