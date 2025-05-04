package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.ExportWithdrawlHistoryRequest;
import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawlSummaryResponse;
import edu.cfd.e_learningPlatform.service.TransactionsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.http.client.methods.HttpHead;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class TransactionsController {

    TransactionsService transactionsService;

    //Lịch sử rút tiền user
    @GetMapping("/user/{id}/earnings-summary")
    public ResponseEntity<EarningsSummaryResponse> getEarningsSummary(@PathVariable String id){
        EarningsSummaryResponse summary = transactionsService.getEarningsSummary(id);
        return ResponseEntity.ok(summary);
    }

    //Tất cả lịch sử rút tiền
    @GetMapping("/withdrawlTransaction")
    public ResponseEntity<List<WithdrawTransactionResponse>> getAllWithdrawls(){
        List<WithdrawTransactionResponse> withdrawls = transactionsService.getAllWithdrawals();
        return ResponseEntity.ok(withdrawls);
    }

    //thống kê withdraw user
    @GetMapping("/withdrawl-summary/{userId}")
    public ResponseEntity<Map<String, List<WithdrawlSummaryResponse>>> getWithdrawlSummary(@PathVariable String userId){
        Map<String, List<WithdrawlSummaryResponse>> summary = transactionsService.getWithdrawalSummary(userId);
        return ResponseEntity.ok(summary);
    }

    // thống kê tiền vào user
    @GetMapping("/withdrawl-summary-payment/{userId}")
    public ResponseEntity<Map<String, List<WithdrawlSummaryResponse>>> getWithdrawSummaryPayment(@PathVariable String userId){
        Map<String, List<WithdrawlSummaryResponse>> summaryPayment = transactionsService.getWithdrawSummaryPayment(userId);
        return ResponseEntity.ok(summaryPayment);
    }

    //thống kê transaction withdraw admin
    @GetMapping("/summary/{timeFrame}")
    public ResponseEntity<List<WithdrawlSummaryResponse>> getAllTransactionsSummary(@PathVariable String timeFrame) {
        List<WithdrawlSummaryResponse> summary = transactionsService.summarizeAllTransactions(timeFrame);
        return ResponseEntity.ok(summary);
    }

    //Thống kê transaction payment admin
    @GetMapping("/summaryPayment/{timeFrame}")
    public ResponseEntity<List<WithdrawlSummaryResponse>> getAllTransactionsSummaryPayment(@PathVariable String timeFrame) {
        List<WithdrawlSummaryResponse> summary = transactionsService.summarizwAllTransactionPayments(timeFrame);
        return ResponseEntity.ok(summary);
    }


    @PostMapping("/export/withdrawl-history")
    public ResponseEntity<byte[]> exportWithdrawlHistory(
            @RequestBody ExportWithdrawlHistoryRequest request) {

        byte[] file = transactionsService.exportWithdrawlHistoryToExcel(
                request.getUserId(),
                request.getStartDate(),
                request.getEndDate()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "lich-su-rut-tien-"+ ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @PostMapping("/export/money-history")
    public ResponseEntity<byte[]> exportMoneyHistory(
            @RequestBody ExportWithdrawlHistoryRequest request) {

        byte[] file = transactionsService.exportMoneyHistoryToExcel(
                request.getUserId(),
                request.getStartDate(),
                request.getEndDate()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "lich-su-thanh-toan" + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @PostMapping("/export/all/withdraw-history")
    public ResponseEntity<byte[]> exportAllWithdrawlHistory(
            @RequestBody ExportWithdrawlHistoryRequest request) {

        byte[] file = transactionsService.exportAllTransactionToExcel(
                request.getStartDate(),
                request.getEndDate()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "tat-ca-lich-su-rut-tien" + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @PostMapping("/export/all/money-history")
    public ResponseEntity<byte[]> exportAllMoneyHistory(
            @RequestBody ExportWithdrawlHistoryRequest request) {

        byte[] file = transactionsService.exportAllTransactionPaymentToExcel(
                request.getStartDate(),
                request.getEndDate()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "tat-ca-lich-su-thanh-toan-" + ".xlsx";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

}
