package edu.cfd.e_learningPlatform.service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.mail.MessagingException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import edu.cfd.e_learningPlatform.config.PaypalPaymentIntent;
import edu.cfd.e_learningPlatform.config.PaypalPaymentMethod;
import edu.cfd.e_learningPlatform.dto.PaymentDto;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.PaymentStatus;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.mapstruct.PaymentMapper;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.repository.PaymentStatusRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.PaypalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    public Payment createPayment(
            Double total,
            String currency,
            PaypalPaymentMethod method,
            PaypalPaymentIntent intent,
            String description,
            String cancelUrl,
            String successUrl)
            throws PayPalRESTException {
        validatePaymentParameters(total, currency, method, intent);
        currency = "USD"; // Dù bạn có thể sử dụng VND, nhưng PayPal yêu cầu USD ở đây.
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(
                BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP).toString());

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
    public String processPayment(@RequestParam Double price, @RequestParam Long courseId, @RequestParam String userId)
            throws PayPalRESTException {
        validateProcessPaymentParameters(price, courseId, userId);

        // Lấy thông tin khóa học và người dùng
        Course course =
                courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Sử dụng trực tiếp số tiền đã nhập mà không cần đổi sang USD
        BigDecimal amount = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);

        // Kiểm tra xem số tiền có phải là 0 không
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ.");
        }

        // Kiểm tra xem người dùng đã thanh toán cho khóa học này chưa
        List<edu.cfd.e_learningPlatform.entity.Payment> existingPayments =
                paymentRepository.findByUserIdAndCourseId(userId, courseId);

        // Nếu chưa có thanh toán nào, tạo một thanh toán mới và lưu vào cơ sở dữ liệu
        if (existingPayments.isEmpty()) {
            String cancelUrl = "http://localhost:8081/api/payments/paypal/cancel";
            String successUrl = buildSuccessUrl(courseId, userId, price);

            // Tạo Payment qua PayPal
            Payment payment = createPayment(
                    amount.doubleValue(),
                    "VND",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "Mô tả đơn hàng",
                    cancelUrl,
                    successUrl);

            // Trả về PaymentApprovalLink từ PayPal
            String approvalLink = findApprovalLink(payment)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy liên kết thanh toán."));

            // Lấy paymentId từ PayPal response
            String paymentId = payment.getId();

            // Tạo PaymentDto
            PaymentDto paymentDto = createPaymentDto(payment, course, user, price);

            // Ánh xạ PaymentDto sang Payment entity
            edu.cfd.e_learningPlatform.entity.Payment paymentEntity =
                    PaymentMapper.INSTANCE.paymentDtoToPayment(paymentDto);

            // Lấy PaymentStatus từ cơ sở dữ liệu
            PaymentStatus pendingStatus = paymentStatusRepository
                    .findByStatusName("Peding")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái thanh toán PENDING"));

            // Gán PaymentStatus và paymentId vào Payment
            paymentEntity.setPaymentStatus(pendingStatus);
            paymentEntity.setPaymentId(paymentId); // Cập nhật paymentId từ PayPal

            // Lưu Payment entity vào cơ sở dữ liệu
            paymentRepository.save(paymentEntity);

            // Trả về đường dẫn thanh toán PayPal
            return approvalLink;
        } else {
            // Nếu đã có thanh toán, chỉ cần cập nhật lại dữ liệu vào thanh toán trước đó
            edu.cfd.e_learningPlatform.entity.Payment existingPayment =
                    existingPayments.get(0); // Lấy thanh toán đầu tiên

            // Lấy PaymentStatus từ cơ sở dữ liệu
            PaymentStatus pendingStatus = paymentStatusRepository
                    .findByStatusName("Peding")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái thanh toán PENDING"));

            // Cập nhật các thông tin cần thiết cho thanh toán cũ
            existingPayment.setPrice(BigDecimal.valueOf(price)); // Cập nhật giá trị thanh toán
            existingPayment.setPaymentStatus(pendingStatus); // Set đối tượng PaymentStatus thay vì long

            // Cập nhật paymentId cho thanh toán cũ từ PayPal
            Payment payment = createPayment(
                    amount.doubleValue(),
                    "VND",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "Mô tả đơn hàng",
                    "http://localhost:8081/api/payments/paypal/cancel",
                    buildSuccessUrl(courseId, userId, price));

            // Lấy paymentId từ PayPal response
            String paymentId = payment.getId();
            existingPayment.setPaymentId(paymentId); // Cập nhật paymentId từ PayPal

            // Lưu lại thanh toán đã cập nhật vào cơ sở dữ liệu
            paymentRepository.save(existingPayment);

            // Trả về đường dẫn thanh toán mới từ PayPal
            String approvalLink = findApprovalLink(payment)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy liên kết thanh toán."));

            // Trả về đường dẫn đến trang thanh toán PayPal mới
            return approvalLink;
        }
    }

    private void validatePaymentParameters(
            Double total, String currency, PaypalPaymentMethod method, PaypalPaymentIntent intent) {
        if (total == null || currency == null || method == null || intent == null) {
            throw new IllegalArgumentException("Tham số thanh toán không hợp lệ.");
        }
    }

    private void validateExecutionParameters(String paymentId, String payerId) {
        if (paymentId == null || payerId == null) {
            throw new IllegalArgumentException("ID thanh toán và ID người trả không được để trống.");
        }
    }

    private void validateProcessPaymentParameters(Double price, Long courseId, String userId) {
        if (price == null || courseId == null || userId == null) {
            throw new IllegalArgumentException("Giá, khóa học ID, và người dùng ID không được để trống.");
        }
    }

    private Optional<String> findApprovalLink(Payment payment) {
        return payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .map(Links::getHref)
                .findFirst();
    }

    private PaymentDto createNewPayment(PaymentDto paymentDTO) {
        PaymentDto newPayment = new PaymentDto();
        newPayment.setPaymentId(paymentDTO.getPaymentId());
        newPayment.setPrice(paymentDTO.getPrice());
        newPayment.setPaymentDate(paymentDTO.getPaymentDate());
        newPayment.setUserId(String.valueOf(userRepository
                .findById(paymentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"))));
        newPayment.setCourseId(courseRepository
                .findById(paymentDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại"))
                .getId());
        newPayment.setPaymentStatusId(paymentStatusRepository
                .findById(paymentDTO.getPaymentStatusId())
                .orElseThrow(() -> new RuntimeException("Trạng thái thanh toán không tồn tại"))
                .getId());
        newPayment.setEnrollment(paymentDTO.getEnrollment());
        return newPayment;
    }

    private PaymentDto createPaymentDto(Payment paypalPayment, Course course, User user, Double price) {
        return new PaymentDto(
                paypalPayment.getId(),
                BigDecimal.valueOf(price),
                LocalDateTime.now(),
                user.getId(),
                course.getId(),
                3L, // Trạng thái đang tiến hành
                false // Đánh dấu đã đăng ký
                );
    }

    private String buildSuccessUrl(Long courseId, String userId, Double price) {
        return String.format(
                "http://localhost:8080/api/payments/paypal/success?courseId=%d&userId=%s&price=%.2f",
                courseId, userId, price);
    }

    @Override
    public void updatePaymentStatus(String paymentId, long statusId) {
        edu.cfd.e_learningPlatform.entity.Payment payment =
                (edu.cfd.e_learningPlatform.entity.Payment) paymentRepository
                        .findByPaymentId(paymentId)
                        .orElseThrow(
                                () -> new RuntimeException("Không tìm thấy thanh toán cho paymentId: " + paymentId));

        // Cập nhật trạng thái thanh toán
        payment.setPaymentStatus(paymentStatusRepository
                .findById(statusId)
                .orElseThrow(() -> new RuntimeException("Trạng thái thanh toán không tồn tại")));
        paymentRepository.save(payment);
    }

    public String getUserEmailById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getEmail).orElse(null);
    }

    @Override
    public void sendPaymentConfirmationEmail(String emailAddress, String paymentId, Double price)
            throws MessagingException {
        String subject = "Payment Confirmation";
        String body = String.format(
                "<html><body>" + "<h1>Payment Confirmation</h1>"
                        + "<p>Your payment has been processed successfully.</p>"
                        + "<ul>"
                        + "<li><strong>Payment ID:</strong> %s</li>"
                        + "<li><strong>Amount:</strong> %.2f VND</li>"
                        + "</ul>"
                        + "</body></html>",
                paymentId, price);

        emailService.sendEmail(emailAddress, subject, body);
    }

    @Override
    public void cancelPayment(String paymentId) {
        edu.cfd.e_learningPlatform.entity.Payment payment =
                (edu.cfd.e_learningPlatform.entity.Payment) paymentRepository
                        .findByPaymentId(paymentId)
                        .orElseThrow(
                                () -> new RuntimeException("Không tìm thấy thanh toán cho paymentId: " + paymentId));

        // Đặt trạng thái thanh toán là Failed (Thất bại) khi hủy
        payment.setPaymentStatus(paymentStatusRepository
                .findById(2L) // 2L là mã trạng thái Failed
                .orElseThrow(() -> new RuntimeException("Trạng thái thanh toán không tồn tại")));
        payment.setEnrollment(false); // Đặt enrollment thành false
        paymentRepository.save(payment);
    }

    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi phút
    public void checkPendingPayments() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        List<edu.cfd.e_learningPlatform.entity.Payment> pendingPayments =
                paymentRepository.findAllByPaymentStatusId(3L); // 3L là mã trạng thái "Pending"

        for (edu.cfd.e_learningPlatform.entity.Payment payment : pendingPayments) {
            updatePaymentStatus(payment.getPaymentId(), 2L); // 2L là mã trạng thái "Failed"
        }
    }

    @Override
    public boolean hasUserPaidForCourse(String userId, Long courseId) {
        // Kiểm tra xem người dùng đã thanh toán cho khóa học này chưa
        List<edu.cfd.e_learningPlatform.entity.Payment> payments =
                paymentRepository.findByUserIdAndCourseIdAndPaymentStatusId(
                        userId, courseId, 1L); // Status 1L là Completed
        return !payments.isEmpty();
    }
}
