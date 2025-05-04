package edu.cfd.e_learningPlatform.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private String paymentId;
    private BigDecimal price;
    private LocalDateTime paymentDate;
    private Long courseId;
    private Long paymentStatusId;
    private Boolean  enrollment;
    private String courseTitle;  // Thêm trường courseTitle vào PaymentDto
}