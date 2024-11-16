package edu.cfd.e_learningPlatform.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.PaypalService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/payments/paypal")
public class PayPalController {

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam("price") Double price,
                                      @RequestParam("courseId") Long courseId,
                                      @RequestParam("userId") String userId) {
        try {
            String paymentUrl = paypalService.processPayment(price, courseId, userId);
            return ResponseEntity.ok("{\"paymentUrl\":\"" + paymentUrl + "\"}");
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi trong việc tạo thanh toán: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId,
                                             @RequestParam("PayerID") String payerId,
                                             @RequestParam(value = "courseId") Long courseId,
                                             @RequestParam(value = "userId") String userId,
                                             @RequestParam(value = "price") Double price) {
        try {
            Payment paypalPayment = paypalService.executePayment(paymentId, payerId);

            // Kiểm tra trạng thái của thanh toán
            Long statusId;
            if ("approved".equals(paypalPayment.getState())) {
                statusId = 1L; // Completed
            } else if ("failed".equals(paypalPayment.getState())) {
                statusId = 2L; // Failed
            } else {
                statusId = 3L; // Pending
            }

            // Cập nhật trạng thái thanh toán
            paypalService.updatePaymentStatus(paymentId, statusId);

            // Nếu thanh toán thành công, gửi email xác nhận thanh toán cho người dùng
            if (statusId.equals(1L)) { // Nếu trạng thái là Completed
                String email = paypalService.getUserEmailById(userId);
                if (email != null) {
                    paypalService.sendPaymentConfirmationEmail(email, paymentId, price);
                }
                String redirectUrl = String.format("http://localhost:8081/vue/enrollment-confirmation?paymentId=%s&courseId=%d", paymentId, courseId);

                return ResponseEntity.status(HttpStatus.FOUND) // 302 Found
                        .location(URI.create(redirectUrl))
                        .build();
            }

            return ResponseEntity.status(HttpStatus.OK).body("Thanh toán không thành công.");

        } catch (PayPalRESTException | MessagingException e) {
            return ResponseEntity.status(500).body("Lỗi khi xử lý thanh toán: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancel(@RequestParam("paymentId") String paymentId) {
        // Hủy thanh toán
        paypalService.cancelPayment(paymentId); // Giả định bạn có phương thức cancelPayment trong PaypalService
        return ResponseEntity.ok("Thanh toán đã bị hủy");
    }
}
