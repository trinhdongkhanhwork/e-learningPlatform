package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.ConfirmWithdrawRequest;
import edu.cfd.e_learningPlatform.dto.request.WalletRequest;
import edu.cfd.e_learningPlatform.dto.response.WalletResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawResponse;
import edu.cfd.e_learningPlatform.service.WalletService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.service.WithdrawService;
@RestController
@RequestMapping("/api/withdraw")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawController {

    WithdrawService withdrawService;


    @PostMapping()
    public ResponseEntity<WithdrawResponse> withdraw(@RequestBody @Valid WalletRequest walletRequest) {
        WithdrawResponse response = withdrawService.withdraw(walletRequest.getUserId(), walletRequest.getAmount());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<WithdrawResponse> confirmWithdraw(@RequestBody @Valid ConfirmWithdrawRequest request) {
        WithdrawResponse response = withdrawService.confirmWithdraw(
                request.getUserId(),
                request.getOtp(),
                request.getWithdrawId()
        );
        return ResponseEntity.ok(response);
    }
}
