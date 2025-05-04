package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import edu.cfd.e_learningPlatform.service.VNPayService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments/vnpay")
public class VNPayController {

    private static final Logger logger = LoggerFactory.getLogger(VNPayController.class);

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponseDto> createOrder(@RequestBody PaymentRequest request) {
        logger.info("Received payment request: {}", request);
        PaymentResponseDto response = vnPayService.handlePayment(request);
        logger.info("Payment response: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public void successPay(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "vnp_TxnRef") String transactionNo,
                           @RequestParam(value = "courseIds") String courseIds) throws MessagingException {
        logger.info("Received success callback for transaction: {}", transactionNo);
        vnPayService.successPay(request, transactionNo);
        try {
            String redirectUrl = String.format("http://localhost:8081/vue/payment-success?courseIds=%s&transactionNo=%s",
                    courseIds, transactionNo);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            logger.error("Error redirecting to frontend: {}", e.getMessage());
            throw new RuntimeException("Error redirecting to frontend", e);
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        logger.info("Payment canceled");
        return ResponseEntity.ok(vnPayService.cancelPay());
    }
}