package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawlSummaryResponse;
import edu.cfd.e_learningPlatform.service.TransactionsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class TransactionsController {

    TransactionsService transactionsService;

    @GetMapping("/user/{id}/earnings-summary")
    public ResponseEntity<EarningsSummaryResponse> getEarningsSummary(@PathVariable String id){
        EarningsSummaryResponse summary = transactionsService.getEarningsSummary(id);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/withdrawlTransaction")
    public ResponseEntity<List<WithdrawTransactionResponse>> getAllWithdrawls(){
        List<WithdrawTransactionResponse> withdrawls = transactionsService.getAllWithdrawals();
        return ResponseEntity.ok(withdrawls);
    }

    @GetMapping("/withdrawl-summary/{userId}")
    public ResponseEntity<Map<String, List<WithdrawlSummaryResponse>>> getWithdrawlSummary(@PathVariable String userId){
        Map<String, List<WithdrawlSummaryResponse>> summary = transactionsService.getWithdrawalSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/{timeFrame}")
    public ResponseEntity<List<WithdrawlSummaryResponse>> getAllTransactionsSummary(@PathVariable String timeFrame) {
        List<WithdrawlSummaryResponse> summary = transactionsService.summarizeAllTransactions(timeFrame);
        return ResponseEntity.ok(summary);
    }

}
