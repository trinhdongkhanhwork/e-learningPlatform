
package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    PaymentResponseDto createOrder(int total, String orderInfo, String urlReturn);

    void successPay(HttpServletRequest request, String transactionNo) throws MessagingException;

    String cancelPay();

    PaymentResponseDto handlePayment(PaymentRequest request);
}
