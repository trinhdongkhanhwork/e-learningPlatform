package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;

public interface TransactionsService {

    EarningsSummaryResponse getEarningsSummary(String userId);
}
