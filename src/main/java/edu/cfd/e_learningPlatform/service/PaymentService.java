package edu.cfd.e_learningPlatform.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.response.PaymentResponse;

@Service
public interface PaymentService {
    boolean IsPaymentCourse(Long idCourse, String userId);

    List<PaymentResponse> getPaymentHistoryByUserId(String userId);
}
