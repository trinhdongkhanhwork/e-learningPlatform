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

    private static String transactionId; // Biến tĩnh lưu mã giao dịch để sử dụng trong các phương thức

    // Tạo URL thanh toán gửi đến VNPay
    @Override
    public PaymentResponseDto createOrder(int total, String orderInfo, String urlReturn) {
        String vnp_Version = "2.1.0"; // Phiên bản API của VNPay
        String vnp_Command = "pay"; // Lệnh yêu cầu thanh toán
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8); // Mã giao dịch ngẫu nhiên 8 chữ số
        String vnp_IpAddr = "127.0.0.1"; // Địa chỉ IP của server (ở đây dùng localhost)
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode; // Mã thương mại từ VNPay
        String orderType = "order-type"; // Loại đơn hàng (giá trị mặc định)

        transactionId = vnp_TxnRef; // Lưu mã giao dịch để sử dụng sau
        int amountInVND = total * 25000 * 100; // Quy đổi USD sang VND (x25,000) và nhân 100 theo yêu cầu VNPay

        // Tạo map chứa các tham số gửi đến VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInVND));
        vnp_Params.put("vnp_CurrCode", "VND"); // Đơn vị tiền tệ là VND
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo); // Thông tin đơn hàng
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn"); // Ngôn ngữ giao diện (tiếng Việt)
        vnp_Params.put("vnp_ReturnUrl", urlReturn + VNPayConfig.vnp_Returnurl); // URL trả về sau thanh toán
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Thêm thời gian tạo và hết hạn cho giao dịch
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7")); // Lấy giờ Việt Nam
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss"); // Định dạng thời gian
        String vnp_CreateDate = formatter.format(cld.getTime()); // Thời gian tạo giao dịch
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // Cộng 15 phút làm thời gian hết hạn
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo dữ liệu hash và chữ ký bảo mật
        String hashData = buildHashData(vnp_Params); // Chuỗi dữ liệu để mã hóa
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData); // Chữ ký HMAC-SHA512
        String queryUrl = buildQueryUrl(vnp_Params, vnp_SecureHash); // URL hoàn chỉnh gửi đến VNPay

        // Trả về đối tượng chứa URL thanh toán, số tiền và mã giao dịch
        return new PaymentResponseDto(
                VNPayConfig.vnp_PayUrl + "?" + queryUrl,
                BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP), // Định dạng số tiền với 2 chữ số thập phân
                vnp_TxnRef
        );
    }

    // Xử lý yêu cầu thanh toán từ người dùng
    @Override
    public PaymentResponseDto handlePayment(PaymentRequest request) {
        // Kiểm tra dữ liệu đầu vào không được null
        Objects.requireNonNull(request, "PaymentRequest không được null");
        Objects.requireNonNull(request.getCourseIds(), "Course IDs không được null hoặc rỗng");
        Objects.requireNonNull(request.getUserId(), "User ID không được null");

        BigDecimal totalAmount = BigDecimal.ZERO; // Tổng số tiền ban đầu là 0
        List<Course> coursesToPay = new ArrayList<>(); // Danh sách khóa học cần thanh toán
        Map<Long, Payment> existingPaymentsToUpdate = new HashMap<>(); // Lưu các thanh toán đã tồn tại để cập nhật

        // Duyệt qua từng khóa học trong yêu cầu
        for (Long courseId : request.getCourseIds()) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại: " + courseId));

            // Kiểm tra xem người dùng đã thanh toán cho khóa học này chưa
            List<Payment> existingPayments = paymentRepository.findByUserIdAndCourseId(request.getUserId(), courseId);
            if (!existingPayments.isEmpty()) {
                Payment existingPayment = existingPayments.get(0);
                if (existingPayment.getPaymentStatus().getId() == 1L) { // Trạng thái COMPLETED
                    throw new IllegalArgumentException("Bạn đã thanh toán thành công cho khóa học: " + courseId);
                }
                totalAmount = totalAmount.add(course.getPrice()); // Cộng giá khóa học vào tổng tiền
                existingPaymentsToUpdate.put(courseId, existingPayment); // Lưu để cập nhật sau
                coursesToPay.add(course);
            } else {
                totalAmount = totalAmount.add(course.getPrice()); // Cộng giá nếu chưa có thanh toán
                coursesToPay.add(course);
            }
        }

        // Kiểm tra nếu không có khóa học hoặc tổng tiền không hợp lệ
        if (coursesToPay.isEmpty() || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(coursesToPay.isEmpty() ? "Không có khóa học nào cần thanh toán" : "Tổng tiền không hợp lệ");
        }

        String successUrl = buildSuccessUrl(request.getCourseIds(), request.getUserId()); // Tạo URL trả về khi thành công
        PaymentResponseDto response = createOrder(totalAmount.intValue(), "Payment for " + coursesToPay.size() + " courses", successUrl);

        // Lấy trạng thái PENDING từ cơ sở dữ liệu
        PaymentStatus pendingStatus = paymentStatusRepository.findByStatusName("PENDING")
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái PENDING không tồn tại"));

        // Tạo hoặc cập nhật bản ghi thanh toán cho từng khóa học
        for (Course course : coursesToPay) {
            Long courseId = course.getId();
            Payment payment = existingPaymentsToUpdate.getOrDefault(courseId, new Payment()); // Lấy bản ghi cũ hoặc tạo mới
            payment.setCourse(course);
            payment.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại")));
            payment.setPrice(course.getPrice());
            payment.setPaymentId(response.getPaymentId()); // Gán mã giao dịch từ VNPay
            payment.setPaymentStatus(pendingStatus); // Đặt trạng thái là PENDING
            payment.setEnrollment(false); // Chưa ghi danh khóa học
            payment.setPaymentDate(LocalDateTime.now()); // Thời gian tạo giao dịch
            paymentRepository.save(payment); // Lưu vào cơ sở dữ liệu
        }

        return response; // Trả về URL thanh toán
    }

    // Xử lý khi thanh toán thành công
    @Override
    public void successPay(HttpServletRequest request, String transactionNo) throws MessagingException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode"); // Mã phản hồi từ VNPay
        String vnp_Amount = request.getParameter("vnp_Amount"); // Số tiền từ VNPay
        String userId = sanitizeUserId(request.getParameter("userId")); // Lấy và làm sạch userId

        if ("00".equals(vnp_ResponseCode)) { // Nếu mã phản hồi là "00" (thành công)
            double amountInUSD = Double.parseDouble(vnp_Amount) / 100 / 25000.0; // Quy đổi từ VND sang USD
            BigDecimal priceInUSD = BigDecimal.valueOf(amountInUSD).setScale(2, RoundingMode.HALF_UP);

            // Tìm các bản ghi thanh toán theo mã giao dịch
            List<Payment> payments = paymentRepository.findByPaymentId(transactionNo);
            if (payments.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy thanh toán cho transactionNo: " + transactionNo);
            }

            // Lấy trạng thái COMPLETED
            PaymentStatus completedStatus = paymentStatusRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Trạng thái COMPLETED không tồn tại"));

            // Cập nhật trạng thái thanh toán
            payments.forEach(payment -> {
                payment.setPaymentStatus(completedStatus); // Đặt trạng thái là COMPLETED
                payment.setEnrollment(true); // Kích hoạt ghi danh khóa học
                paymentRepository.save(payment);
            });

            // Gửi email xác nhận
            String email = getUserEmailById(userId);
            if (email != null) {
                emailService.sendPaymentConfirmationEmail(email, transactionNo, priceInUSD.doubleValue());
            }
        } else {
            updatePaymentStatus(transactionNo, 2L); // Nếu thất bại, cập nhật trạng thái FAILED
            throw new RuntimeException("Payment failed or not completed.");
        }
    }

    // Hủy thanh toán
    @Override
    public String cancelPay() {
        return "Payment was canceled."; // Trả về thông báo hủy
    }

    // Kiểm tra các giao dịch PENDING quá hạn
    @Scheduled(fixedRate = 60000) // Chạy mỗi 60 giây
    public void checkPendingPayments() {
        List<Payment> pendingPayments = paymentRepository.findAllByPaymentStatusId(3L); // Lấy danh sách PENDING
        LocalDateTime now = LocalDateTime.now();
        long timeoutMillis = 180000; // Thời gian chờ 3 phút (180,000 ms)

        pendingPayments.forEach(payment -> {
            if (java.time.Duration.between(payment.getPaymentDate(), now).toMillis() >= timeoutMillis) {
                updatePaymentStatus(payment.getPaymentId(), 2L); // Cập nhật thành FAILED nếu quá hạn
            }
        });
    }

    // Tạo chuỗi dữ liệu để mã hóa
    private String buildHashData(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames); // Sắp xếp các tham số theo thứ tự alphabet
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

    // Tạo URL hoàn chỉnh gửi đến VNPay
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
        return query.append("&vnp_SecureHash=").append(secureHash).toString(); // Thêm chữ ký bảo mật
    }

    // Tạo URL trả về khi thanh toán thành công
    private String buildSuccessUrl(List<Long> courseIds, String userId) {
        String courseIdsParam = String.join(",", courseIds.stream().map(String::valueOf).toList());
        return String.format("http://localhost:8080/api/payments/vnpay/success?courseIds=%s&userId=%s",
                courseIdsParam, URLEncoder.encode(userId, StandardCharsets.UTF_8));
    }

    // Làm sạch userId (loại bỏ phần không cần thiết nếu có)
    private String sanitizeUserId(String rawUserId) {
        return (rawUserId != null && rawUserId.contains("/")) ? rawUserId.split("/")[0] : rawUserId;
    }

    // Lấy email của người dùng theo userId
    public String getUserEmailById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getEmail).orElse(null);
    }

    // Cập nhật trạng thái thanh toán
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