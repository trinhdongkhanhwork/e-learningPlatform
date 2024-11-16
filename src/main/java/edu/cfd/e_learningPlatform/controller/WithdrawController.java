package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.WithdrawDto;
import edu.cfd.e_learningPlatform.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    // API tạo yêu cầu rút tiền
    @PostMapping("/request/{userId}")
    public ResponseEntity<?> requestWithdraw(
            @PathVariable("userId") String userId,
            @RequestParam("price") BigDecimal price) {
        try {
            WithdrawDto withdrawDto = withdrawService.requestWithdraw(userId, price);
            return ResponseEntity.ok(withdrawDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã xảy ra lỗi không mong muốn.");
        }
    }

    // API tính tổng số tiền
    @GetMapping("/total-payments/{userId}")
    public ResponseEntity<BigDecimal> getTotalPaymentsForInstructor(@PathVariable String userId) {
        BigDecimal totalPayments = withdrawService.calculateTotalPaymentsForInstructor(userId);
        return ResponseEntity.ok(totalPayments);
    }

    // API lấy lịch sử rút tiền
    @GetMapping("/history/{userId}")
    public List<WithdrawDto> getWithdrawalHistory(@PathVariable String userId) {
        return withdrawService.getWithdrawalHistory(userId);
    }

    // Endpoint xác thực OTP
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmWithdraw(@RequestParam Long withdrawId, @RequestParam String otp) {
        // Gọi service để xác thực yêu cầu rút tiền
        withdrawService.confirmWithdraw(withdrawId, otp);
        return ResponseEntity.ok("Withdraw confirmed");
    }
}


