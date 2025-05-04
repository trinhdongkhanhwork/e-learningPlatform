package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.ConfirmWithdrawRequest;
import edu.cfd.e_learningPlatform.dto.request.WalletRequest;
import edu.cfd.e_learningPlatform.dto.response.WalletResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawResponse;
import edu.cfd.e_learningPlatform.entity.Wallet;
import edu.cfd.e_learningPlatform.service.WalletService;
import edu.cfd.e_learningPlatform.service.WithdrawService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {

    WalletService walletService;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<WalletResponse> getWalletBalance(@PathVariable String userId){
        WalletResponse response = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(response);
    }

}
