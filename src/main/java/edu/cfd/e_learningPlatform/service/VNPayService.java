package edu.cfd.e_learningPlatform.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VNPayService {


    String createOrder(int total, String orderInfo, String urlReturn);

    String getTransactionId();

    int orderReturn(HttpServletRequest request);

    void successPay(HttpServletRequest request, String transactionNo) throws MessagingException;

    String cancelPay();

    Map<String, String> handlePayment(Long courseId, String userId, Integer price);
}
