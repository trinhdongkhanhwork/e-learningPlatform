package edu.cfd.e_learningPlatform.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import edu.cfd.e_learningPlatform.service.PaypalService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/payments/paypal")
public class PayPalController {

    @Autowired
    private PaypalService paypalService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody PaymentRequest request) {
        try {
            PaymentResponseDto response = paypalService.processMultiplePayments(request);
            return ResponseEntity.ok(response);
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi trong việc tạo thanh toán: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> successPay(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("courseIds") String courseIds,
            @RequestParam("userId") String userId,
            @RequestParam("price") Double price) {
        try {
            // Gọi service để xử lý thanh toán thành công
            String redirectUrl = paypalService.handlePaymentSuccess(paymentId, payerId, courseIds, userId, price);
            if (redirectUrl != null) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(redirectUrl))
                        .build();
            }
            return ResponseEntity.ok("Thanh toán không thành công.");
        } catch (PayPalRESTException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xử lý thanh toán: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(@RequestParam("paymentId") String paymentId) {
        paypalService.cancelPayment(paymentId);
        return ResponseEntity.ok("Thanh toán đã bị hủy");
    }
}