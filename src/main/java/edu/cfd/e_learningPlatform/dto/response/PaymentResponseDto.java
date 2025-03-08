package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private String paymentUrl;     // URL để người dùng thanh toán qua PayPal
    private BigDecimal totalAmount; // Tổng tiền thanh toán
    private String paymentId;       // ID thanh toán từ PayPal
}