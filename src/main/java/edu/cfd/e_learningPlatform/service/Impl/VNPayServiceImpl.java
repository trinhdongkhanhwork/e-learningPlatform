package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.config.VNPayConfig;
import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.entity.PaymentStatus;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.repository.PaymentStatusRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.VNPayService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {

    UserRepository userRepository;
    PaymentRepository paymentRepository;
    CourseRepository courseRepository;
    PaymentStatusRepository paymentStatusRepository;
    EmailService emailService;

    private static String transactionId;
    @Override
    public PaymentResponseDto createOrder(int total, String orderInfo, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";

        transactionId = vnp_TxnRef;
        int amountInVND = total * 25000 * 100; // Quy đổi USD sang VND và nhân 100

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInVND));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", urlReturn + VNPayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        String hashData = buildHashData(vnp_Params);
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData);
        String queryUrl = buildQueryUrl(vnp_Params, vnp_SecureHash);

        return new PaymentResponseDto(
                VNPayConfig.vnp_PayUrl + "?" + queryUrl,
                BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP),
                vnp_TxnRef
        );
    }

    @Override
    public PaymentResponseDto handlePayment(PaymentRequest request) {
        Objects.requireNonNull(request, "PaymentRequest không được null");
        Objects.requireNonNull(request.getCourseIds(), "Course IDs không được null hoặc rỗng");
        Objects.requireNonNull(request.getUserId(), "User ID không được null");

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Course> coursesToPay = new ArrayList<>();
        Map<Long, Payment> existingPaymentsToUpdate = new HashMap<>();

        for (Long courseId : request.getCourseIds()) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại: " + courseId));

            List<Payment> existingPayments = paymentRepository.findByUserIdAndCourseId(request.getUserId(), courseId);
            if (!existingPayments.isEmpty()) {
                Payment existingPayment = existingPayments.get(0);
                if (existingPayment.getPaymentStatus().getId() == 1L) {
                    throw new IllegalArgumentException("Bạn đã thanh toán thành công cho khóa học: " + courseId);
                }
                totalAmount = totalAmount.add(course.getPrice());
                existingPaymentsToUpdate.put(courseId, existingPayment);
                coursesToPay.add(course);
            } else {
                totalAmount = totalAmount.add(course.getPrice());
                coursesToPay.add(course);
            }
        }

        if (coursesToPay.isEmpty() || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(coursesToPay.isEmpty() ? "Không có khóa học nào cần thanh toán" : "Tổng tiền không hợp lệ");
        }

        String successUrl = buildSuccessUrl(request.getCourseIds(), request.getUserId());
        PaymentResponseDto response = createOrder(totalAmount.intValue(), "Payment for " + coursesToPay.size() + " courses", successUrl);

        PaymentStatus pendingStatus = paymentStatusRepository.findByStatusName("PENDING")
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái PENDING không tồn tại"));

        for (Course course : coursesToPay) {
            Long courseId = course.getId();
            Payment payment = existingPaymentsToUpdate.getOrDefault(courseId, new Payment());
            payment.setCourse(course);
            payment.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại")));
            payment.setPrice(course.getPrice());
            payment.setPaymentId(response.getPaymentId());
            payment.setPaymentStatus(pendingStatus);
            payment.setEnrollment(false);
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
        }

        return response;
    }
    @Override
    public void successPay(HttpServletRequest request, String transactionNo) throws MessagingException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_Amount = request.getParameter("vnp_Amount");
        String userId = sanitizeUserId(request.getParameter("userId"));
        if ("00".equals(vnp_ResponseCode)) {
            double amountInUSD = Double.parseDouble(vnp_Amount) / 100 / 25000.0;
            BigDecimal priceInUSD = BigDecimal.valueOf(amountInUSD).setScale(2, RoundingMode.HALF_UP);

            List<Payment> payments = paymentRepository.findByPaymentId(transactionNo);
            if (payments.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy thanh toán cho transactionNo: " + transactionNo);
            }

            PaymentStatus completedStatus = paymentStatusRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Trạng thái COMPLETED không tồn tại"));

            payments.forEach(payment -> {
                payment.setPaymentStatus(completedStatus);
                payment.setEnrollment(true);
                paymentRepository.save(payment);
            });

            String email = getUserEmailById(userId);
            if (email != null) {
                emailService.sendPaymentConfirmationEmail(email, transactionNo, priceInUSD.doubleValue());
            }
        } else {
            updatePaymentStatus(transactionNo, 2L); // FAILED
            throw new RuntimeException("Payment failed or not completed.");
        }
    }

    @Override
    public String cancelPay() {
        return "Payment was canceled.";
    }

    @Scheduled(fixedRate = 60000)
    public void checkPendingPayments() {
        List<Payment> pendingPayments = paymentRepository.findAllByPaymentStatusId(3L);
        LocalDateTime now = LocalDateTime.now();
        long timeoutMillis = 180000;

        pendingPayments.forEach(payment -> {
            if (java.time.Duration.between(payment.getPaymentDate(), now).toMillis() >= timeoutMillis) {
                updatePaymentStatus(payment.getPaymentId(), 2L);
            }
        });
    }

    private String buildHashData(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) hashData.append('&');
            }
        }
        return hashData.toString();
    }

    private String buildQueryUrl(Map<String, String> params, String secureHash) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) query.append('&');
            }
        }
        return query.append("&vnp_SecureHash=").append(secureHash).toString();
    }
    private String buildSuccessUrl(List<Long> courseIds, String userId) {
        String courseIdsParam = String.join(",", courseIds.stream().map(String::valueOf).toList());
        return String.format("http://localhost:8080/api/payments/vnpay/success?courseIds=%s&userId=%s",
                courseIdsParam, URLEncoder.encode(userId, StandardCharsets.UTF_8));
    }
    private String sanitizeUserId(String rawUserId) {
        return (rawUserId != null && rawUserId.contains("/")) ? rawUserId.split("/")[0] : rawUserId;
    }

    public String getUserEmailById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getEmail).orElse(null);
    }

    private void updatePaymentStatus(String paymentId, long statusId) {
        List<Payment> payments = paymentRepository.findByPaymentId(paymentId);
        if (payments.isEmpty()) throw new IllegalArgumentException("Không tìm thấy thanh toán cho paymentId: " + paymentId);

        PaymentStatus status = paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái thanh toán không tồn tại"));

        payments.forEach(payment -> {
            payment.setPaymentStatus(status);
            paymentRepository.save(payment);
        });
    }
}