package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    boolean IsPaymentCourse(Long idCourse, String userId);

    List<PaymentResponse> getPaymentHistoryByUserId(String userId);
}
