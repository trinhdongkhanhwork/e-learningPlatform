
package edu.cfd.e_learningPlatform.service;


import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import edu.cfd.e_learningPlatform.config.PaypalPaymentIntent;
import edu.cfd.e_learningPlatform.config.PaypalPaymentMethod;
import edu.cfd.e_learningPlatform.dto.request.PaymentRequest;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponseDto;
import jakarta.mail.MessagingException;

public interface PaypalService {
    Payment createPayment(Double total, String currency, PaypalPaymentMethod method, PaypalPaymentIntent intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    PaymentResponseDto processMultiplePayments(PaymentRequest request) throws PayPalRESTException;

    void updatePaymentStatus(String paymentId, long statusId);

    void cancelPayment(String paymentId);

    String getUserEmailById(String userId);

    String handlePaymentSuccess(String paymentId, String payerId, String courseIds, String userId, Double price)
            throws PayPalRESTException, MessagingException;
}
