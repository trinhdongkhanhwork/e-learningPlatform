package edu.cfd.e_learningPlatform.service.Impl;


import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.service.EnrollmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
     PaymentRepository paymentRepository;
     CourseRepository courseRepository;

    @Override
    public void confirmEnrollment(String paymentId, Long courseId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán cho paymentId: " + paymentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học cho courseId: " + courseId));

        // Đặt enrollment thành true khi người dùng xác nhận
        payment.setEnrollment(true);
        paymentRepository.save(payment);
    }
}
