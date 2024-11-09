package edu.cfd.e_learningPlatform.service;


import edu.cfd.e_learningPlatform.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
}
