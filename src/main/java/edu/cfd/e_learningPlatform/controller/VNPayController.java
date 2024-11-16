package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.service.VNPayService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/payments/vnpay")
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> createOrder(@RequestParam("price") Integer price,
                                                           @RequestParam("courseId") Long courseId,
                                                           @RequestParam("userId") String userId) {
        return ResponseEntity.ok(vnPayService.handlePayment(courseId, userId, price));
    }

    @GetMapping("/success")
    public void successPay(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "vnp_TxnRef") String transactionNo,
                           @RequestParam(value = "courseId") Long courseId) throws MessagingException {

        // Xử lý logic thanh toán thành công
        vnPayService.successPay(request, transactionNo);

        try {
            // Tạo URL để chuyển hướng tới trang xác nhận đăng ký
            String redirectUrl = String.format("http://localhost:8081/vue/enrollment-confirmation?courseId=%d&paymentId=%s",
                    courseId, transactionNo);
            // Chuyển hướng tới URL
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace(); // Ghi log nếu xảy ra lỗi trong quá trình chuyển hướng
        }
    }
    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok(vnPayService.cancelPay());
    }
}

