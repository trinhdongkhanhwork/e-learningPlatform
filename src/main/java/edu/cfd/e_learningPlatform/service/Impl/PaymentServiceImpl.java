package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.response.PaymentResponse;
import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.mapstruct.PaymentMapper;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Override
    public boolean IsPaymentCourse(Long idCourse, String userId) { ///lỗi ở đây
        Payment payment = paymentRepository.findByPaymentisErollment(idCourse,userId);
        if(payment == null){
            System.out.println(idCourse);
            System.out.println(userId);
            return false;
        }else {
            return true;
        }
    }
    @Override
    public List<PaymentResponse> getPaymentHistoryByUserId(String userId) {
        // Fetch payments for the specific user
        List<Payment> payments = paymentRepository.findByUser_Id(userId);

        // Map the payments to PaymentResponse objects
        return payments.stream()
                .map(paymentMapper::paymentToPaymentResponse)
                .collect(Collectors.toList());
    }
}