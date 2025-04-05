package edu.cfd.e_learningPlatform.utils;

import java.security.SecureRandom;

public class OtpUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // Sinh số ngẫu nhiên 6 chữ số
        return String.valueOf(otp);
    }
}
