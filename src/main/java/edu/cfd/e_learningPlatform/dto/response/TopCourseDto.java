package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopCourseDto {
    private Long courseId;     // ID khóa học
    private String courseName; // Tên khóa học
    private Long totalSold;    // Số lần bán
    private String imageUrl;     // Đường dẫn đến ảnh đại diện của khóa học
    private Double totalRevenue; // Doanh thu từ khóa học
}
