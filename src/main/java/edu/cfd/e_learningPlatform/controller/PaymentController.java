package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.PaymentResponse;
import edu.cfd.e_learningPlatform.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    PaymentService paymentService;

    @GetMapping("/isPayment/{idCourse}/{userId}")
    public ResponseEntity<Boolean> isPayment(@PathVariable Long idCourse,@PathVariable String userId){
        return ResponseEntity.ok(paymentService.IsPaymentCourse(idCourse,userId));
    } ///lỗi ở đây
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory(@PathVariable String userId) {
        List<PaymentResponse> paymentHistory = paymentService.getPaymentHistoryByUserId(userId);
        return ResponseEntity.ok(paymentHistory);
    }
}
