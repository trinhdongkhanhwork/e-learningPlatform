package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawlSummaryResponse;
import edu.cfd.e_learningPlatform.entity.TransactionPayment;
import edu.cfd.e_learningPlatform.entity.Transactions;

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
}
