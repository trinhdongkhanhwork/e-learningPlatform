package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private String paymentId;
    private BigDecimal price;
    private LocalDateTime paymentDate;
    private String userId;
    private Long courseId;
    private Long paymentStatusId;
    private Boolean  enrollment;

}
