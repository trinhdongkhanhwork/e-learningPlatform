package edu.cfd.e_learningPlatform.service.Impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import edu.cfd.e_learningPlatform.config.PaypalPaymentIntent;
import edu.cfd.e_learningPlatform.config.PaypalPaymentMethod;
import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.PaymentStatus;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.repository.PaymentStatusRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.PaypalService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {

    APIContext apiContext;
    PaymentRepository paymentRepository;
    PaymentStatusRepository paymentStatusRepository;
    CourseRepository courseRepository;
    UserRepository userRepository;
    EmailService emailService;

    @Override
    public Payment createPayment(Double total, String currency, PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent, String description,
                                 String cancelUrl, String successUrl) throws PayPalRESTException {
        validatePaymentParameters(total, currency, method, intent);
        currency = "USD"; // Dù bạn có thể sử dụng VND, nhưng PayPal yêu cầu USD ở đây.
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP).toString());

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = Collections.singletonList(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        validateExecutionParameters(paymentId, payerId);

        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecute);
    }
    @Override
    public PaymentResponseDto processMultiplePayments(PaymentRequest request) throws PayPalRESTException {
        // Validate input
        if (request.getCourseIds() == null || request.getCourseIds().isEmpty() || request.getUserId() == null) {
            throw new IllegalArgumentException("Danh sách khóa học và ID người dùng không được để trống.");
        }

        // Lấy thông tin người dùng
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Tính tổng tiền và kiểm tra thanh toán
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Course> coursesToPay = new ArrayList<>(); // Danh sách khóa học cần tạo hoặc xử lý thanh toán
        Map<Long, edu.cfd.e_learningPlatform.entity.Payment> existingPaymentsToUpdate = new HashMap<>(); // Lưu các bản ghi cần cập nhật

        for (Long courseId : request.getCourseIds()) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại: " + courseId));

            // Kiểm tra xem đã có bản ghi thanh toán nào cho khóa học này chưa
            List<edu.cfd.e_learningPlatform.entity.Payment> existingPayments =
                    paymentRepository.findByUserIdAndCourseId(user.getId(), courseId);

            if (!existingPayments.isEmpty()) {
                edu.cfd.e_learningPlatform.entity.Payment existingPayment = existingPayments.get(0);
                if (existingPayment.getPaymentStatus().getId() == 1L) { // COMPLETED
                    throw new IllegalArgumentException("Bạn đã thanh toán thành công cho khóa học: " + courseId);
                } else {
                    // Nếu là PENDING (3L) hoặc FAILED (2L), thêm vào danh sách để cập nhật
                    totalAmount = totalAmount.add(course.getPrice());
                    existingPaymentsToUpdate.put(courseId, existingPayment);
                    coursesToPay.add(course);
                }
            } else {
                // Nếu chưa có bản ghi, thêm vào danh sách để tạo mới
                totalAmount = totalAmount.add(course.getPrice());
                coursesToPay.add(course);
            }
        }

        if (coursesToPay.isEmpty()) {
            throw new IllegalArgumentException("Không có khóa học nào cần thanh toán.");
        }

        // Tạo URL
        String cancelUrl = "http://localhost:8081/api/payments/paypal/cancel";
        String successUrl = buildSuccessUrl(request.getCourseIds(), request.getUserId(), totalAmount.doubleValue());

        // Tạo thanh toán PayPal
        Payment payment = createPayment(
                totalAmount.doubleValue(),
                "USD",
                PaypalPaymentMethod.paypal,
                PaypalPaymentIntent.sale,
                "Thanh toán cho " + coursesToPay.size() + " khóa học",
                cancelUrl,
                successUrl
        );

        String approvalLink = findApprovalLink(payment)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy liên kết thanh toán."));
        String paymentId = payment.getId();

        // Lưu hoặc cập nhật bản ghi Payment
        PaymentStatus pendingStatus = paymentStatusRepository.findByStatusName("PENDING")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái PENDING"));

        for (Course course : coursesToPay) {
            Long courseId = course.getId();
            if (existingPaymentsToUpdate.containsKey(courseId)) {
                // Cập nhật bản ghi hiện có
                edu.cfd.e_learningPlatform.entity.Payment paymentEntity = existingPaymentsToUpdate.get(courseId);
                paymentEntity.setPaymentId(paymentId);
                paymentEntity.setPaymentStatus(pendingStatus);
                paymentRepository.save(paymentEntity);
            } else {
                // Tạo bản ghi mới nếu chưa tồn tại
                edu.cfd.e_learningPlatform.entity.Payment paymentEntity = new edu.cfd.e_learningPlatform.entity.Payment();
                paymentEntity.setCourse(course);
                paymentEntity.setUser(user);
                paymentEntity.setPrice(course.getPrice());
                paymentEntity.setPaymentId(paymentId);
                paymentEntity.setPaymentStatus(pendingStatus);
                paymentEntity.setEnrollment(false);
                paymentRepository.save(paymentEntity);
            }
        }

        // Trả về response
        PaymentResponseDto response = new PaymentResponseDto();
        response.setPaymentUrl(approvalLink);
        response.setTotalAmount(totalAmount);
        response.setPaymentId(paymentId);
        return response;
    }
    private void validatePaymentParameters(Double total, String currency, PaypalPaymentMethod method, PaypalPaymentIntent intent) {
        if (total == null || currency == null || method == null || intent == null) {
            throw new IllegalArgumentException("Tham số thanh toán không hợp lệ.");
        }
    }

    private void validateExecutionParameters(String paymentId, String payerId) {
        if (paymentId == null || payerId == null) {
            throw new IllegalArgumentException("ID thanh toán và ID người trả không được để trống.");
        }
    }

    private Optional<String> findApprovalLink(Payment payment) {
        return payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .map(Links::getHref)
                .findFirst();
    }

    private String buildSuccessUrl(List<Long> courseIds, String userId, Double totalAmount) {
        String courseIdsParam = String.join(",", courseIds.stream().map(String::valueOf).toList());
        return String.format("http://localhost:8080/api/payments/paypal/success?courseIds=%s&userId=%s&price=%.2f",
                courseIdsParam, userId, totalAmount);
    }

    @Override
    public void updatePaymentStatus(String paymentId, long statusId) {
        // Lấy danh sách các Payment theo paymentId
        List<edu.cfd.e_learningPlatform.entity.Payment> paymentEntities = paymentRepository.findByPaymentId(paymentId);
        if (paymentEntities.isEmpty()) {
            throw new RuntimeException("Không tìm thấy thanh toán cho paymentId: " + paymentId);
        }

        // Lấy trạng thái thanh toán
        PaymentStatus paymentStatus = paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Trạng thái thanh toán không tồn tại"));

        // Cập nhật trạng thái cho tất cả các bản ghi
        for (edu.cfd.e_learningPlatform.entity.Payment payment : paymentEntities) {
            payment.setPaymentStatus(paymentStatus);
            paymentRepository.save(payment);
        }
    }

    public String getUserEmailById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getEmail).orElse(null);
    }
    @Override
    public void cancelPayment(String paymentId) {
        // Lấy danh sách các Payment theo paymentId
        List<edu.cfd.e_learningPlatform.entity.Payment> paymentEntities = paymentRepository.findByPaymentId(paymentId);
        if (paymentEntities.isEmpty()) {
            throw new RuntimeException("Không tìm thấy thanh toán cho paymentId: " + paymentId);
        }
        // Lấy trạng thái Failed
        PaymentStatus failedStatus = paymentStatusRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Trạng thái thanh toán không tồn tại"));
        // Cập nhật trạng thái và enrollment cho tất cả các bản ghi
        for (edu.cfd.e_learningPlatform.entity.Payment payment : paymentEntities) {
            payment.setPaymentStatus(failedStatus);
            payment.setEnrollment(false);
            paymentRepository.save(payment);
        }
    }

    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi phút
    public void checkPendingPayments() {
        List<edu.cfd.e_learningPlatform.entity.Payment> pendingPayments =
                paymentRepository.findAllByPaymentStatusId(3L); // 3L là mã trạng thái "Pending"

        LocalDateTime now = LocalDateTime.now();
        long timeoutMillis = 180000; // 3 phút = 180000 milliseconds

        for (edu.cfd.e_learningPlatform.entity.Payment payment : pendingPayments) {
            // Tính khoảng thời gian kể từ khi tạo bản ghi
            long timeElapsedMillis = java.time.Duration.between(payment.getPaymentDate(), now).toMillis();

            // Nếu đã quá 3 phút, chuyển sang trạng thái FAILED
            if (timeElapsedMillis >= timeoutMillis) {
                updatePaymentStatus(payment.getPaymentId(), 2L); // 2L là mã trạng thái "Failed"
            }
        }
    }
    @Override
    public String handlePaymentSuccess(String paymentId, String payerId, String courseIds, String userId, Double price)
            throws PayPalRESTException, MessagingException {
        Payment paypalPayment = executePayment(paymentId, payerId);
        Long statusId = "approved".equals(paypalPayment.getState()) ? 1L : "failed".equals(paypalPayment.getState()) ? 2L : 3L;

        List<edu.cfd.e_learningPlatform.entity.Payment> payments = paymentRepository.findByPaymentId(paymentId);
        if (payments.isEmpty()) throw new IllegalArgumentException("Không tìm thấy thanh toán cho paymentId: " + paymentId);

        PaymentStatus status = paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái thanh toán không tồn tại: " + statusId));

        payments.forEach(payment -> {
            payment.setPaymentStatus(status);
            if (statusId.equals(1L)) payment.setEnrollment(true);
            paymentRepository.save(payment);
        });

        if (statusId.equals(1L)) {
            String email = getUserEmailById(userId);
            if (email != null) {
                emailService.sendPaymentConfirmationEmail(email, paymentId, price);
            }
            return String.format("http://localhost:8081/vue/payment-success", paymentId, courseIds);
        }
        return null;
    }
}