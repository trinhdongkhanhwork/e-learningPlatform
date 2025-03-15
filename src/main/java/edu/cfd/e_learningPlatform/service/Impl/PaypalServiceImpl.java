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

    APIContext apiContext; // Đối tượng cấu hình API PayPal
    PaymentRepository paymentRepository; // Repository để quản lý bản ghi thanh toán
    PaymentStatusRepository paymentStatusRepository; // Repository để quản lý trạng thái thanh toán
    CourseRepository courseRepository; // Repository để quản lý khóa học
    UserRepository userRepository; // Repository để quản lý người dùng
    EmailService emailService; // Dịch vụ gửi email

    // Tạo yêu cầu thanh toán PayPal
    @Override
    public Payment createPayment(Double total, String currency, PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent, String description,
                                 String cancelUrl, String successUrl) throws PayPalRESTException {
        validatePaymentParameters(total, currency, method, intent); // Kiểm tra tham số đầu vào
        currency = "USD"; // Đặt cố định là USD vì PayPal yêu cầu (có thể thay đổi tùy cấu hình)

        Amount amount = new Amount(); // Đối tượng chứa thông tin số tiền
        amount.setCurrency(currency); // Đơn vị tiền tệ
        amount.setTotal(BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP).toString()); // Số tiền với 2 chữ số thập phân

        Transaction transaction = new Transaction(); // Đối tượng giao dịch
        transaction.setDescription(description); // Mô tả giao dịch
        transaction.setAmount(amount); // Gán số tiền vào giao dịch

        List<Transaction> transactions = Collections.singletonList(transaction); // Danh sách giao dịch (ở đây chỉ có 1)
        Payer payer = new Payer(); // Đối tượng người trả tiền
        payer.setPaymentMethod(method.toString()); // Phương thức thanh toán (PayPal)

        Payment payment = new Payment(); // Đối tượng thanh toán PayPal
        payment.setIntent(intent.toString()); // Ý định thanh toán (ví dụ: sale)
        payment.setPayer(payer); // Gán thông tin người trả
        payment.setTransactions(transactions); // Gán danh sách giao dịch

        RedirectUrls redirectUrls = new RedirectUrls(); // Đối tượng chứa URL trả về
        redirectUrls.setCancelUrl(cancelUrl); // URL khi hủy thanh toán
        redirectUrls.setReturnUrl(successUrl); // URL khi thanh toán thành công
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext); // Tạo thanh toán qua PayPal API và trả về đối tượng Payment
    }

    // Thực thi thanh toán sau khi người dùng xác nhận
    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        validateExecutionParameters(paymentId, payerId); // Kiểm tra tham số đầu vào

        Payment payment = new Payment(); // Tạo đối tượng thanh toán
        payment.setId(paymentId); // Gán ID thanh toán từ PayPal
        PaymentExecution paymentExecute = new PaymentExecution(); // Đối tượng thực thi thanh toán
        paymentExecute.setPayerId(payerId); // Gán ID người trả từ PayPal

        return payment.execute(apiContext, paymentExecute); // Thực thi thanh toán qua API và trả về kết quả
    }

    // Xử lý yêu cầu thanh toán cho nhiều khóa học
    @Override
    public PaymentResponseDto processMultiplePayments(PaymentRequest request) throws PayPalRESTException {
        // Kiểm tra dữ liệu đầu vào
        if (request.getCourseIds() == null || request.getCourseIds().isEmpty() || request.getUserId() == null) {
            throw new IllegalArgumentException("Danh sách khóa học và ID người dùng không được để trống.");
        }

        // Lấy thông tin người dùng
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Khởi tạo biến để tính toán và lưu trữ
        BigDecimal totalAmount = BigDecimal.ZERO; // Tổng số tiền ban đầu là 0
        List<Course> coursesToPay = new ArrayList<>(); // Danh sách khóa học cần thanh toán
        Map<Long, edu.cfd.e_learningPlatform.entity.Payment> existingPaymentsToUpdate = new HashMap<>(); // Lưu bản ghi cũ cần cập nhật

        // Duyệt qua từng khóa học trong yêu cầu
        for (Long courseId : request.getCourseIds()) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại: " + courseId));

            // Kiểm tra xem có bản ghi thanh toán nào cho khóa học này chưa
            List<edu.cfd.e_learningPlatform.entity.Payment> existingPayments =
                    paymentRepository.findByUserIdAndCourseId(user.getId(), courseId);

            if (!existingPayments.isEmpty()) {
                edu.cfd.e_learningPlatform.entity.Payment existingPayment = existingPayments.get(0);
                if (existingPayment.getPaymentStatus().getId() == 1L) { // Nếu đã thanh toán thành công (COMPLETED)
                    throw new IllegalArgumentException("Bạn đã thanh toán thành công cho khóa học: " + courseId);
                } else {
                    // Nếu là PENDING hoặc FAILED, thêm vào danh sách để cập nhật
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

        // Tạo URL trả về
        String cancelUrl = "http://localhost:8081/api/payments/paypal/cancel"; // URL khi hủy
        String successUrl = buildSuccessUrl(request.getCourseIds(), request.getUserId(), totalAmount.doubleValue()); // URL khi thành công

        // Tạo thanh toán qua PayPal
        Payment payment = createPayment(
                totalAmount.doubleValue(),
                "USD",
                PaypalPaymentMethod.paypal,
                PaypalPaymentIntent.sale,
                "Thanh toán cho " + coursesToPay.size() + " khóa học",
                cancelUrl,
                successUrl
        );

        // Lấy liên kết phê duyệt thanh toán từ PayPal
        String approvalLink = findApprovalLink(payment)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy liên kết thanh toán."));
        String paymentId = payment.getId(); // Lấy ID thanh toán từ PayPal

        // Lấy trạng thái PENDING
        PaymentStatus pendingStatus = paymentStatusRepository.findByStatusName("PENDING")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái PENDING"));

        // Lưu hoặc cập nhật bản ghi thanh toán
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

        // Trả về đối tượng phản hồi với URL thanh toán
        PaymentResponseDto response = new PaymentResponseDto();
        response.setPaymentUrl(approvalLink);
        response.setTotalAmount(totalAmount);
        response.setPaymentId(paymentId);
        return response;
    }

    // Kiểm tra tham số đầu vào cho createPayment
    private void validatePaymentParameters(Double total, String currency, PaypalPaymentMethod method, PaypalPaymentIntent intent) {
        if (total == null || currency == null || method == null || intent == null) {
            throw new IllegalArgumentException("Tham số thanh toán không hợp lệ.");
        }
    }

    // Kiểm tra tham số đầu vào cho executePayment
    private void validateExecutionParameters(String paymentId, String payerId) {
        if (paymentId == null || payerId == null) {
            throw new IllegalArgumentException("ID thanh toán và ID người trả không được để trống.");
        }
    }

    // Tìm liên kết phê duyệt từ danh sách link của PayPal
    private Optional<String> findApprovalLink(Payment payment) {
        return payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel())) // Lọc link có rel là "approval_url"
                .map(Links::getHref) // Lấy URL
                .findFirst(); // Trả về link đầu tiên (nếu có)
    }

    // Tạo URL trả về khi thanh toán thành công
    private String buildSuccessUrl(List<Long> courseIds, String userId, Double totalAmount) {
        String courseIdsParam = String.join(",", courseIds.stream().map(String::valueOf).toList()); // Chuyển danh sách courseIds thành chuỗi
        return String.format("http://localhost:8080/api/payments/paypal/success?courseIds=%s&userId=%s&price=%.2f",
                courseIdsParam, userId, totalAmount); // Tạo URL với các tham số
    }

    // Cập nhật trạng thái thanh toán
    @Override
    public void updatePaymentStatus(String paymentId, long statusId) {
        List<edu.cfd.e_learningPlatform.entity.Payment> paymentEntities = paymentRepository.findByPaymentId(paymentId);
        if (paymentEntities.isEmpty()) {
            throw new RuntimeException("Không tìm thấy thanh toán cho paymentId: " + paymentId);
        }

        PaymentStatus paymentStatus = paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Trạng thái thanh toán không tồn tại"));

        for (edu.cfd.e_learningPlatform.entity.Payment payment : paymentEntities) {
            payment.setPaymentStatus(paymentStatus); // Cập nhật trạng thái
            paymentRepository.save(payment); // Lưu vào cơ sở dữ liệu
        }
    }

    // Lấy email người dùng theo ID
    public String getUserEmailById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getEmail).orElse(null); // Trả về email hoặc null nếu không tìm thấy
    }

    // Hủy thanh toán
    @Override
    public void cancelPayment(String paymentId) {
        List<edu.cfd.e_learningPlatform.entity.Payment> paymentEntities = paymentRepository.findByPaymentId(paymentId);
        if (paymentEntities.isEmpty()) {
            throw new RuntimeException("Không tìm thấy thanh toán cho paymentId: " + paymentId);
        }

        PaymentStatus failedStatus = paymentStatusRepository.findById(2L) // Lấy trạng thái FAILED
                .orElseThrow(() -> new RuntimeException("Trạng thái thanh toán không tồn tại"));

        for (edu.cfd.e_learningPlatform.entity.Payment payment : paymentEntities) {
            payment.setPaymentStatus(failedStatus); // Đặt trạng thái là FAILED
            payment.setEnrollment(false); // Tắt ghi danh khóa học
            paymentRepository.save(payment); // Lưu lại
        }
    }

    // Kiểm tra các thanh toán PENDING quá hạn
    @Scheduled(fixedRate = 60000) // Chạy mỗi 60 giây
    public void checkPendingPayments() {
        List<edu.cfd.e_learningPlatform.entity.Payment> pendingPayments =
                paymentRepository.findAllByPaymentStatusId(3L); // Lấy danh sách thanh toán PENDING (3L)

        LocalDateTime now = LocalDateTime.now();
        long timeoutMillis = 180000; // Thời gian chờ 3 phút

        for (edu.cfd.e_learningPlatform.entity.Payment payment : pendingPayments) {
            long timeElapsedMillis = java.time.Duration.between(payment.getPaymentDate(), now).toMillis();
            if (timeElapsedMillis >= timeoutMillis) { // Nếu quá 3 phút
                updatePaymentStatus(payment.getPaymentId(), 2L); // Cập nhật thành FAILED (2L)
            }
        }
    }

    // Xử lý khi thanh toán thành công
    @Override
    public String handlePaymentSuccess(String paymentId, String payerId, String courseIds, String userId, Double price)
            throws PayPalRESTException, MessagingException {
        Payment paypalPayment = executePayment(paymentId, payerId); // Thực thi thanh toán
        Long statusId = "approved".equals(paypalPayment.getState()) ? 1L : "failed".equals(paypalPayment.getState()) ? 2L : 3L; // Xác định trạng thái

        List<edu.cfd.e_learningPlatform.entity.Payment> payments = paymentRepository.findByPaymentId(paymentId);
        if (payments.isEmpty()) throw new IllegalArgumentException("Không tìm thấy thanh toán cho paymentId: " + paymentId);

        PaymentStatus status = paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái thanh toán không tồn tại: " + statusId));

        payments.forEach(payment -> {
            payment.setPaymentStatus(status); // Cập nhật trạng thái
            if (statusId.equals(1L)) payment.setEnrollment(true); // Nếu thành công, bật ghi danh
            paymentRepository.save(payment); // Lưu lại
        });

        if (statusId.equals(1L)) { // Nếu thanh toán thành công
            String email = getUserEmailById(userId);
            if (email != null) {
                emailService.sendPaymentConfirmationEmail(email, paymentId, price); // Gửi email xác nhận
            }
            return "http://localhost:8081/vue/payment-success"; // Trả về URL thành công cho frontend
        }
        return null; // Nếu không thành công, trả về null
    }
}