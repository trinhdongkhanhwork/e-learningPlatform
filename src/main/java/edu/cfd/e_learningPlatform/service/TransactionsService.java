package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;

import java.util.List;

public interface TransactionsService {

    EarningsSummaryResponse getEarningsSummary(String userId);
    List<WithdrawTransactionResponse> getAllWithdrawals();
}
