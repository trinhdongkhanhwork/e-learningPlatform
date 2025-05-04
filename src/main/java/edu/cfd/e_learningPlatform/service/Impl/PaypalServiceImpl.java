package edu.cfd.e_learningPlatform.service.Impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import edu.cfd.e_learningPlatform.config.PaypalPaymentIntent;
import edu.cfd.e_learningPlatform.config.PaypalPaymentMethod;
import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import edu.cfd.e_learningPlatform.dto.response.WalletResponse;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.PaymentStatus;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.repository.PaymentStatusRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.PaypalService;
import edu.cfd.e_learningPlatform.service.WalletService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    APIContext apiContext;
    PaymentRepository paymentRepository;
    PaymentStatusRepository paymentStatusRepository;
    CourseRepository courseRepository;
    UserRepository userRepository;
    EmailService emailService;
    WalletService walletService;

    @Override
    public Payment createPayment(BigDecimal total, String currency, PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent, String description,
                                 String cancelUrl, String successUrl) throws PayPalRESTException {

        if (total == null || currency == null || method == null || intent == null) {
            throw new IllegalArgumentException("Thông tin thanh toán không hợp lệ.");
        }

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(total.setScale(2, RoundingMode.HALF_UP).toPlainString()); // KHÔNG dùng toString()

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    @Override
    public PaymentResponseDto processMultiplePayments(PaymentRequest request) throws PayPalRESTException {
        if (request.getCourseIds() == null || request.getCourseIds().isEmpty() || request.getUserId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn khóa học và người dùng.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Course> coursesToPay = new ArrayList<>();
        Map<Long, edu.cfd.e_learningPlatform.entity.Payment> existingPaymentsToUpdate = new HashMap<>();

        for (Long courseId : request.getCourseIds()) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại: " + courseId));

            List<edu.cfd.e_learningPlatform.entity.Payment> existingPayments =
                    paymentRepository.findByUserIdAndCourseId(user.getId(), courseId);

            if (!existingPayments.isEmpty()) {
                edu.cfd.e_learningPlatform.entity.Payment existing = existingPayments.get(0);
                if (existing.getPaymentStatus().getId() == 1L) {
                    throw new IllegalArgumentException("Bạn đã thanh toán khóa học: " + courseId);
                } else {
                    totalAmount = totalAmount.add(course.getPrice());
                    existingPaymentsToUpdate.put(courseId, existing);
                    coursesToPay.add(course);
                }
            } else {
                totalAmount = totalAmount.add(course.getPrice());
                coursesToPay.add(course);
            }
        }

        if (coursesToPay.isEmpty()) {
            throw new IllegalArgumentException("Không có khóa học cần thanh toán.");
        }

        String cancelUrl = "http://localhost:8081/api/payments/paypal/cancel";
        String successUrl = buildSuccessUrl(request.getCourseIds(), request.getUserId(), totalAmount);

        Payment payment = createPayment(
                totalAmount,
                "USD",
                PaypalPaymentMethod.paypal,
                PaypalPaymentIntent.sale,
                "Thanh toán " + coursesToPay.size() + " khóa học",
                cancelUrl,
                successUrl
        );

        String approvalLink = findApprovalLink(payment)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy link thanh toán."));
        String paymentId = payment.getId();

        PaymentStatus pending = paymentStatusRepository.findByStatusName("PENDING")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái PENDING"));

        for (Course course : coursesToPay) {
            edu.cfd.e_learningPlatform.entity.Payment p;
            if (existingPaymentsToUpdate.containsKey(course.getId())) {
                p = existingPaymentsToUpdate.get(course.getId());
            } else {
                p = new edu.cfd.e_learningPlatform.entity.Payment();
                p.setCourse(course);
                p.setUser(user);
                p.setPrice(course.getPrice());
            }

            p.setPaymentId(paymentId);
            p.setPaymentStatus(pending);
            p.setEnrollment(false);
            paymentRepository.save(p);
        }

        return PaymentResponseDto.builder()
                .paymentUrl(approvalLink)
                .totalAmount(totalAmount)
                .paymentId(paymentId)
                .build();
    }

    private Optional<String> findApprovalLink(Payment payment) {
        return payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .map(Links::getHref)
                .findFirst();
    }

    private String buildSuccessUrl(List<Long> courseIds, String userId, BigDecimal totalAmount) {
        String courseIdsParam = String.join(",", courseIds.stream().map(String::valueOf).toList());
        return String.format(Locale.US,
                "http://localhost:8080/api/payments/paypal/success?courseIds=%s&userId=%s&price=%.2f",
                courseIdsParam, userId, totalAmount.setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        return payment.execute(apiContext, execution);
    }

    @Override
    public void updatePaymentStatus(String paymentId, long statusId) {
        List<edu.cfd.e_learningPlatform.entity.Payment> payments = paymentRepository.findByPaymentId(paymentId);
        PaymentStatus status = paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái"));

        for (edu.cfd.e_learningPlatform.entity.Payment p : payments) {
            p.setPaymentStatus(status);
            paymentRepository.save(p);
        }
    }

    @Override
    public String getUserEmailById(String userId) {
        return userRepository.findById(userId)
                .map(User::getEmail)
                .orElse(null);
    }

    @Override
    public void cancelPayment(String paymentId) {
        List<edu.cfd.e_learningPlatform.entity.Payment> payments = paymentRepository.findByPaymentId(paymentId);
        PaymentStatus failed = paymentStatusRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái FAILED"));

        for (edu.cfd.e_learningPlatform.entity.Payment p : payments) {
            p.setPaymentStatus(failed);
            p.setEnrollment(false);
            paymentRepository.save(p);
        }
    }

    @Override
    public String handlePaymentSuccess(String paymentId, String payerId, String courseIds, String userId, Double price)
            throws PayPalRESTException, MessagingException {
        // Thực thi thanh toán qua PayPal
        Payment paypalPayment = executePayment(paymentId, payerId);

        // Xác định trạng thái thanh toán dựa trên phản hồi từ PayPal
        Long statusId = "approved".equals(paypalPayment.getState()) ? 1L : "failed".equals(paypalPayment.getState()) ? 2L : 3L;

        // Tìm các bản ghi thanh toán liên quan đến paymentId
        List<edu.cfd.e_learningPlatform.entity.Payment> payments = paymentRepository.findByPaymentId(paymentId);
        if (payments.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy thanh toán cho paymentId: " + paymentId);
        }

        // Lấy trạng thái thanh toán từ repository
        PaymentStatus status = paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái thanh toán không tồn tại: " + statusId));

        // Cập nhật trạng thái cho tất cả các bản ghi thanh toán
        payments.forEach(payment -> {
            payment.setPaymentStatus(status); // Cập nhật trạng thái
            if (statusId.equals(1L)) {
                payment.setEnrollment(true); // Nếu thành công, bật ghi danh khóa học
            }
            paymentRepository.save(payment); // Lưu lại
        });

        // Xử lý khi thanh toán thành công
        if (statusId.equals(1L)) { // Nếu thanh toán thành công
            String email = getUserEmailById(userId);
            if (email != null) {
                emailService.sendPaymentConfirmationEmail(email, paymentId, price); // Gửi email xác nhận
            }

            // Cộng tiền vào ví admin và chia lợi nhuận
            BigDecimal totalAmount = BigDecimal.valueOf(price);
            Long courseId = payments.get(0).getCourse().getId(); // Lấy courseId từ payment đầu tiên
            WalletResponse adminWalletResponse = walletService.depositToAdminWallet(courseId, totalAmount);

            return "http://localhost:8081/vue/payment-success"; // Trả về URL thành công cho frontend
        }

        return null;
    }

    @Scheduled(fixedRate = 60000)
    public void checkPendingPayments() {
        List<edu.cfd.e_learningPlatform.entity.Payment> pendingPayments =
                paymentRepository.findAllByPaymentStatusId(3L);

        LocalDateTime now = LocalDateTime.now();
        long timeoutMillis = 180000;

        for (edu.cfd.e_learningPlatform.entity.Payment p : pendingPayments) {
            long elapsed = java.time.Duration.between(p.getPaymentDate(), now).toMillis();
            if (elapsed >= timeoutMillis) {
                updatePaymentStatus(p.getPaymentId(), 2L);
            }
        }
    }
}
