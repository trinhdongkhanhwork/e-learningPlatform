package edu.cfd.e_learningPlatform.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private Double price;
    private Long courseId;
    private String userId;
    private String paymentMethod;

}
