package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawlSummaryResponse;
import edu.cfd.e_learningPlatform.entity.TransactionPayment;
import edu.cfd.e_learningPlatform.entity.Transactions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionsService {

    EarningsSummaryResponse getEarningsSummary(String userId);
    List<WithdrawTransactionResponse> getAllWithdrawals();

    //Transactions
    Map<String, List<WithdrawlSummaryResponse>> getWithdrawalSummary(String userId);
    List<WithdrawlSummaryResponse> summarizeWithdrawls(List<Transactions> withdrawls, String timeFrame);
    List<WithdrawlSummaryResponse> summarizeAllTransactions(String timeFrame);

    //Payment transaction
    Map<String, List<WithdrawlSummaryResponse>> getWithdrawSummaryPayment(String userId);
    List<WithdrawlSummaryResponse> summarizeWithdrawlsPayment(List<TransactionPayment> withdrawls, String timeFrame);
    List<WithdrawlSummaryResponse> summarizwAllTransactionPayments(String timeFrame);


    //Xuất lịch sử rút tiền ra file excel
    byte[] exportWithdrawlHistoryToExcel(String userId, LocalDateTime startDate, LocalDateTime endDate);

    //Xuất lịch sử tiền vào ra file excel
    byte[] exportMoneyHistoryToExcel(String userId, LocalDateTime startDate, LocalDateTime endDate);

    //xuất excel tất cả lịch xử rút tiền excel
    byte[] exportAllTransactionToExcel(LocalDateTime startDate, LocalDateTime endDate);

    //xuất excel tất cả lịch xử thanh toán excel
    byte[] exportAllTransactionPaymentToExcel(LocalDateTime startDate, LocalDateTime endDate);
}
