package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PaymentServiceIplm implements PaymentService {
    PaymentRepository paymentRepository;

    @Override
    public boolean IsPaymentCourse(Long idCourse, String userId) {
        Payment payment = paymentRepository.findByPaymentisErollment(idCourse,userId);
        if(payment == null){
            System.out.println(idCourse);
            System.out.println(userId);
            return false;
        }else {
            return true;
        }
    }
}
