package edu.cfd.e_learningPlatform.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class VNPayConfig {
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_Returnurl = "/api/payments/vnpay/return"; // Đường dẫn trả về phải bao gồm API context
    public static String vnp_TmnCode = "H0EIB7YY"; // Mã thương mại của bạn
    public static String vnp_HashSecret = "PESCCK0961SSFD4MRJ35BKMLJKVMQSCA"; // Khóa bí mật của bạn
    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction"; // Đường dẫn API
    // Hàm băm SHA-256
    public static String sha256(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // Hàm băm tất cả các trường dữ liệu
    public static String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                sb.append(fieldName).append("=").append(fieldValue).append("&");
            }
        }
        // Xóa ký tự '&' ở cuối chuỗi
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return hmacSHA512(vnp_HashSecret, sb.toString());
    }

    // Hàm HMAC SHA-512
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new IllegalArgumentException("Key and data cannot be null");
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error in HMAC calculation", ex);
        }
    }

    // Hàm lấy địa chỉ IP của người dùng
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        return (ipAddress != null) ? ipAddress : request.getRemoteAddr();
    }

    // Hàm tạo số ngẫu nhiên
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(rnd.nextInt(10)); // Tạo một chữ số ngẫu nhiên
        }
        return sb.toString();
    }
}
