package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.config.VNPayConfig;
import edu.cfd.e_learningPlatform.dto.PaymentDto;
import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.mapstruct.PaymentMapper;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {

    public static String transactionId;

    UserRepository userRepository;

    PaymentRepository paymentRepository;

    CourseRepository courseRepository;

    PaymentStatusRepository paymentStatusRepository;

    EmailService emailService;
    @Override
    public String createOrder(int total, String orderInfo, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1"; // Thay thế bằng địa chỉ IP thực tế
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";

        // Lưu mã giao dịch vào biến transactionId
        this.transactionId = vnp_TxnRef;
        // Chuyển đổi USD sang VND và nhân với 100
        int amountInVND = total * 25000; // Quy đổi từ USD sang VND
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amountInVND * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locale = "vn";
        vnp_Params.put("vnp_Locale", locale);

        // URL trả về
        urlReturn += VNPayConfig.vnp_Returnurl; // Đảm bảo urlReturn là đầy đủ
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Tạo ngày giờ
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // Thêm 15 phút vào ngày hết hạn
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo chuỗi để tính toán chữ ký
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                // Tạo dữ liệu hash
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Tạo chuỗi truy vấn
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        return VNPayConfig.vnp_PayUrl + "?" + query.toString(); // Trả về URL thanh toán
    }

    @Override
    public String getTransactionId() {
        return transactionId; // Trả về transaction ID đã lưu
    }

    @Override
    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        // Ghi lại các tham số nhận được từ VNPay để gỡ lỗi
        logRequestParameters(fields);

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (vnp_SecureHash == null) {
            return -1; // Không có chữ ký
        }

        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Kiểm tra chữ ký
        String signValue = VNPayConfig.hashAllFields(fields);

        if (!vnp_SecureHash.equals(signValue)) {
            return -1; // Chữ ký không hợp lệ
        }

        // Lấy mã phản hồi
        String vnpResponseCode = request.getParameter("vnp_ResponseCode");
        if (vnpResponseCode == null) {
            return -1; // Không có mã phản hồi
        } else if ("00".equals(vnpResponseCode)) {
            return 1; // Thanh toán thành công
        } else {
            return 0; // Thanh toán thất bại
        }
    }

    private void logRequestParameters(Map<String, String> fields) {
        System.out.println("Request parameters from VNPay:");
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public Map<String, String> handlePayment(Long courseId, String userId, Integer price) {
        // Tạo URL thành công
        String successUrl = buildSuccessUrl(courseId, userId);
        String paymentUrl = createOrder(price, "Payment for course ID: " + courseId, successUrl);

        // Tạo PaymentDTO và thực thể Payment
        PaymentDto paymentDTO = createNewPaymentDTO(price, userId, courseId);
        Payment newPayment = createNewPayment(paymentDTO);
        newPayment.setPaymentId(transactionId); // Lấy transactionId từ createOrder
        paymentRepository.save(newPayment);

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        return response;
    }

    private String buildSuccessUrl(Long courseId, String userId) {
        return String.format("http://localhost:8080/api/payments/vnpay/success?courseId=%d&userId=%s", courseId, userId);
    }

    private PaymentDto createNewPaymentDTO(Integer price, String userId, Long courseId) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPrice(BigDecimal.valueOf(price));
        paymentDto.setPaymentDate(LocalDateTime.now());
        paymentDto.setUserId(userId);
        paymentDto.setCourseId(courseId);
        paymentDto.setPaymentStatusId(3L); // ID 3cho trạng thái PENDING
        paymentDto.setEnrollment(false); // sửa
        return paymentDto;
    }

    private Payment createNewPayment(PaymentDto paymentDTO) {
        return PaymentMapper.INSTANCE.paymentDtoToPayment(paymentDTO);
    }

    @Override
    public void successPay(HttpServletRequest request, String transactionNo) throws MessagingException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_Amount = request.getParameter("vnp_Amount");

        // Chuyển đổi từ VND sang USD
        double amountInVND = Double.parseDouble(vnp_Amount); // Số tiền thanh toán bằng VND
        double exchangeRate = 25000; // Tỷ giá VND -> USD (Ví dụ: 25,000 VND = 1 USD)
        double amountInUSD = amountInVND / exchangeRate; // Quy đổi ra USD

        Payment existingPayment = (Payment) paymentRepository.findByPaymentId(transactionNo)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán cho transactionNo: " + transactionNo));

        // Cập nhật trạng thái thanh toán
        if ("00".equals(vnp_ResponseCode)) {
            updatePaymentStatus(existingPayment, vnp_Amount, 1L); // ID 1 cho COMPLETED
        } else {
            updatePaymentStatus(existingPayment, vnp_Amount, 2L); // ID 2 cho FAILED
        }

        // Gửi email xác nhận nếu thanh toán thành công
        if ("00".equals(vnp_ResponseCode)) {
            String emailAddress = getUserEmailById(existingPayment.getUser().getId());
            String subject = "Payment confirmation";
            String body = buildEmailBody(transactionNo, amountInUSD); // Truyền số tiền USD vào email body
            emailService.sendEmail(emailAddress, subject, body);
        }
    }

    private String buildEmailBody(String transactionNo, double amountInUSD) {
        // Định dạng lại số tiền để hiển thị chính xác 2 chữ số sau dấu phẩy
        String formattedAmount = String.format("%.2f", amountInUSD);

        return "<html><head><title>Payment Confirmation</title></head><body>"
                + "<h1>Payment Confirmation</h1>"
                + "<p>Your payment has been successfully processed.</p>"
                + "<ul>"
                + "<li><strong>Transaction Number:</strong> " + transactionNo + "</li>"
                + "<li><strong>Amount:</strong> " + formattedAmount + " USD</li>" // Hiển thị số tiền đã định dạng
                + "</ul>"
                + "</body></html>";
    }
    private String getUserEmailById(String userId) {
        return userRepository.findEmailById(userId);
    }

    private void updatePaymentStatus(Payment payment, String vnp_Amount, Long statusId) {
        // Cập nhật trạng thái thanh toán
        payment.setPaymentStatus(paymentStatusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái thanh toán")));
        // Lưu lại thông tin thanh toán vào database
        payment.setEnrollment(false);
        paymentRepository.save(payment);
    }

    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi 1 phút
    public void checkPendingPayments() {
        List<Payment> pendingPayments = paymentRepository.findAllByPaymentStatusId(3L); // ID 3 cho trạng thái PENDING
        for (Payment payment : pendingPayments) {
            // Kiểm tra thời gian tạo thanh toán, nếu đã quá 1 phút thì cập nhật thành FAILED
            if (payment.getPaymentDate().isBefore(LocalDateTime.now().minusMinutes(2))) {
                updatePaymentStatus(payment, payment.getPrice().toString(), 2L); // Cập nhật thành FAILED
            }
        }
    }
    @Override
    public String cancelPay() {
        return "Payment was canceled.";
    }
}



