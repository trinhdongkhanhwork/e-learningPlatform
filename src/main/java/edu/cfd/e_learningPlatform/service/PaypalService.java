package edu.cfd.e_learningPlatform.service;


import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import edu.cfd.e_learningPlatform.config.PaypalPaymentIntent;
import edu.cfd.e_learningPlatform.config.PaypalPaymentMethod;
import jakarta.mail.MessagingException;

public interface PaypalService {


    Payment createPayment(Double total, String currency, PaypalPaymentMethod method, PaypalPaymentIntent intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException, MessagingException;

    String processPayment(Double price, Long courseId, String userId) throws PayPalRESTException;

    void updatePaymentStatus(String paymentId, long statusId);

    void sendPaymentConfirmationEmail(String emailAddress, String paymentId, Double price) throws MessagingException;

    void cancelPayment(String paymentId);

    String getUserEmailById(String userId);
}
