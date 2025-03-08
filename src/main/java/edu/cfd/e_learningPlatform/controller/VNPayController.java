package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import edu.cfd.e_learningPlatform.service.VNPayService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments/vnpay")
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponseDto> createOrder(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(vnPayService.handlePayment(request));
    }

    @GetMapping("/success")
    public void successPay(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "vnp_TxnRef") String transactionNo,
                           @RequestParam(value = "courseIds") String courseIds) throws MessagingException {
        vnPayService.successPay(request, transactionNo);
        try {
            String redirectUrl = String.format("http://localhost:8081/vue/payment-success",
                    courseIds, transactionNo);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //test commit
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok(vnPayService.cancelPay());
    }
}