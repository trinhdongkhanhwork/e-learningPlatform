package edu.cfd.e_learningPlatform.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class PaymentRequest {
    private List<Long> courseIds; // Danh sách ID các khóa học
    private String userId;        // ID người dùng
}
