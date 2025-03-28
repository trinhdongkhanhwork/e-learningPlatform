package edu.cfd.e_learningPlatform.service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import edu.cfd.e_learningPlatform.entity.Video;
import edu.cfd.e_learningPlatform.repository.VideoRepository;

@Service
public class S3ServiceImpl implements edu.cfd.e_learningPlatform.service.S3Service {

    private final AmazonS3 amazonS3;
    private final VideoRepository videoRepository;

    @Value("${s3.bucketName}")
    private String BUCKET_NAME;

    public S3ServiceImpl(AmazonS3 amazonS3, VideoRepository videoRepository) {
        this.amazonS3 = amazonS3;
        this.videoRepository = videoRepository;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        PutObjectRequest putRequest =
                new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream(), new ObjectMetadata());
        amazonS3.putObject(putRequest);
        return "https://" + BUCKET_NAME + ".s3.amazonaws.com/" + fileName + UUID.randomUUID();
    }

    @Override
    public List<Video> uploadFiles(List<MultipartFile> files) throws IOException {
        List<Video> videos = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            long contentLength = file.getSize();

            // Kích thước mỗi phần tải lên
            long partSize = 5 * 1024 * 1024; // 5MB

            // Khởi tạo yêu cầu tải lên nhiều phần
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(BUCKET_NAME, fileName);

            InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(initRequest);

            // PartETags : danh sách các phần đã tải lên thành công
            List<PartETag> partETags = new ArrayList<>();
            try {
                long filePosition = 0; // Vị trí bắt đầu của phần
                for (int i = 1; filePosition < contentLength; i++) {
                    partSize = Math.min(partSize, (contentLength - filePosition));

                    // Tạo yêu cầu tải lên một phần
                    UploadPartRequest uploadRequest = new UploadPartRequest()
                            .withBucketName(BUCKET_NAME)
                            .withKey(fileName)
                            .withUploadId(initResponse.getUploadId())
                            .withPartNumber(i)
                            .withFileOffset(filePosition)
                            .withInputStream(file.getInputStream())
                            .withPartSize(partSize);

                    // Tải lên phần và thêm phản hồi vào danh sách của chúng ta
                    partETags.add(amazonS3.uploadPart(uploadRequest).getPartETag());
                    filePosition += partSize;
                }

                // Hoàn thành tải lên nhiều phần
                CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
                        BUCKET_NAME, fileName, initResponse.getUploadId(), partETags);

                amazonS3.completeMultipartUpload(compRequest);

            } catch (Exception e) {
                // Hủy tải lên nhiều phần nếu có lỗi xảy ra
                amazonS3.abortMultipartUpload(
                        new AbortMultipartUploadRequest(BUCKET_NAME, fileName, initResponse.getUploadId()));
                throw new IOException("Upload failed", e);
            }

            // Lấy thông tin video
            Video video = new Video();
            video.setFileName(fileName);
            video.setDuration(getVideoDuration(file));
            video.setVideoUrl("https://" + BUCKET_NAME + ".s3.amazonaws.com/" + fileName + UUID.randomUUID());
            videos.add(video);

            // Update video list
            Video fileVideo = videoRepository.findByFileName(fileName);
            if (fileVideo != null) {
                fileVideo.setDuration(video.getDuration());
                fileVideo.setVideoUrl(video.getVideoUrl());
                videoRepository.save(fileVideo);
            }
        }
        return videos;
    }

    private String getVideoDuration(MultipartFile file) throws IOException {
        // Sử dụng thư viện Tika để lấy thông tin video
        Tika tika = new Tika();
        // Tạo một đối tượng Metadata để lưu trữ thông tin
        Metadata metadata = new Metadata();
        // Đọc dữ liệu từ file
        try (InputStream stream = file.getInputStream()) {
            // Parse dữ liệu và lưu vào metadata
            tika.parse(stream, metadata);
        }
        // Lấy thời lượng video từ metadata và chuyển đổi sang định dạng chuỗi
        return formatDuration(metadata.get("xmpDM:duration"));
    }

    private String formatDuration(String durationStr) {
        // Chuyển đổi chuỗi thời gian thành số nguyên (giây)
        int durationInSeconds = Integer.parseInt(durationStr);

        // Nếu nhỏ hơn 60 giây, trả về giá trị giây
        return String.valueOf(durationInSeconds);
    }
}
