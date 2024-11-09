package edu.cfd.e_learningPlatform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

@Service
public class FileService {
    private static final String uploadDir = "uploads/courses";  // Thư mục chứa tài liệu khóa học

    // Tạo tài liệu và lưu vào file
    public static String createAndSaveCourseMaterial(Long courseId, String courseTitle) throws IOException {
        Path directoryPath = Paths.get(uploadDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath); // Tạo thư mục nếu chưa tồn tại
        }

        // Tạo nội dung tài liệu (ví dụ, chỉ là thông tin khóa học)
        String content = "Course ID: " + courseId + "\nCourse Title: " + courseTitle + "\nDate: " + java.time.LocalDate.now();

        // Tạo đường dẫn cho tài liệu
        Path filePath = directoryPath.resolve("course-" + courseId + "-material.pdf");

        // Ghi nội dung vào file (ở đây đơn giản chỉ ghi text)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(content);
        }

        return filePath.toString(); // Trả về đường dẫn đã lưu
    }

    public static String saveCourseMaterial(Long courseId, MultipartFile file) throws IOException {
        Path directoryPath = Paths.get(uploadDir);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath); // Tạo thư mục nếu chưa tồn tại
        }

        // Tạo đường dẫn cho tài liệu (ví dụ: course-1-material.pdf)
        Path filePath = directoryPath.resolve("course-" + courseId + "-material.pdf");

        // Lưu file vào thư mục
        file.transferTo(filePath.toFile());

        return filePath.toString(); // Trả về đường dẫn của file đã lưu
    }
}
