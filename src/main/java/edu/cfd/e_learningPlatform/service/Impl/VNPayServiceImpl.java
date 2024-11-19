package edu.cfd.e_learningPlatform.service.Impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.config.VNPayConfig;
import edu.cfd.e_learningPlatform.dto.PaymentDto;
import edu.cfd.e_learningPlatform.entity.Payment;
import edu.cfd.e_learningPlatform.mapstruct.PaymentMapper;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.VNPayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {

    public static String transactionId;

    UserRepository userRepository;

    PaymentRepository paymentRepository;

    //    CourseRepository courseRepository;
    //
    //    PaymentStatusRepository paymentStatusRepository;

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
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
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
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
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

    @Override
    public Map<String, String> handlePayment(Long courseId, String userId, Integer price) {
        // Tạo URL thành công
        String successUrl = buildSuccessUrl(courseId, userId);
        String paymentUrl = createOrder(price, "Payment for course ID: " + courseId, successUrl);

        // Không lưu thông tin thanh toán vào cơ sở dữ liệu tại đây
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        return response;
    }

    @Override
    public void successPay(HttpServletRequest request, String transactionNo) throws MessagingException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_Amount = request.getParameter("vnp_Amount");

        String rawUserId = request.getParameter("userId");
        String userId;

        // Loại bỏ phần không hợp lệ nếu có
        if (rawUserId != null && rawUserId.contains("/")) {
            userId = rawUserId.split("/")[0]; // Lấy phần trước dấu "/"
        } else {
            userId = rawUserId;
        }
        String courseIdParam = request.getParameter("courseId");
        Long courseId = null;

        try {
            courseId = Long.parseLong(courseIdParam);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid courseId format: " + courseIdParam);
        }

        // Kiểm tra response code từ VNPay
        if ("00".equals(vnp_ResponseCode)) {
            // Thanh toán thành công
            double amountInVND = Double.parseDouble(vnp_Amount) / 100; // Chia cho 100 để lấy đúng giá trị VND
            double exchangeRate = 25000.0; // Tỉ giá USD -> VND
            double amountInUSD = amountInVND / exchangeRate;
            // Định dạng lại amountInUSD với 2 chữ số thập phân
            String formattedAmountInUSD = String.format("%.2f", amountInUSD);
            // Tạo DTO thanh toán
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setPrice(new BigDecimal(formattedAmountInUSD));
            paymentDto.setPaymentDate(LocalDateTime.now());
            paymentDto.setUserId(userId);
            paymentDto.setCourseId(courseId);
            paymentDto.setPaymentStatusId(1L); // COMPLETED
            paymentDto.setEnrollment(true); // Đã ghi danh

            Payment payment = PaymentMapper.INSTANCE.paymentDtoToPayment(paymentDto);
            payment.setPaymentId(transactionNo); // Gán mã giao dịch

            paymentRepository.save(payment);

            // Gửi email xác nhận
            String emailAddress = getUserEmailById(userId);
            String subject = "Payment confirmation";
            String body = buildEmailBody(transactionNo, amountInUSD);
            emailService.sendEmail(emailAddress, subject, body);
        } else {
            throw new RuntimeException("Payment failed or not completed.");
        }
    }

    private String buildSuccessUrl(Long courseId, String userId) {
        try {
            return String.format(
                    "http://localhost:8080/api/payments/vnpay/success?courseId=%d&userId=%s",
                    courseId, URLEncoder.encode(userId, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error encoding userId", e);
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

    //    private void updatePaymentStatus(Payment payment, String vnp_Amount, Long statusId) {
    //        // Cập nhật trạng thái thanh toán
    //        payment.setPaymentStatus(paymentStatusRepository.findById(statusId)
    //                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái thanh toán")));
    //        // Lưu lại thông tin thanh toán vào database
    //        payment.setEnrollment(false);
    //        paymentRepository.save(payment);
    //    }
    @Override
    public String cancelPay() {
        return "Payment was canceled.";
    }
}
