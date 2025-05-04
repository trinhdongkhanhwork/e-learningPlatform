package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.response.WithdrawResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import edu.cfd.e_learningPlatform.entity.Transactions;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Wallet;
import edu.cfd.e_learningPlatform.entity.Withdraw;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.repository.TransactionRespository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.repository.WalletRespository;
import edu.cfd.e_learningPlatform.repository.WithdrawRepository;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.TransactionsService;
import edu.cfd.e_learningPlatform.service.WithdrawService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {

    WalletRespository walletRespository;
    UserRepository userRepository;
    TransactionRespository transactionRespository;
    TransactionsService transactionsService;
    WithdrawRepository withdrawRepository;
    EmailService emailService;
    PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public WithdrawResponse withdraw(String userId, BigDecimal amount) {
        // Tìm user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy ví admin
        User admin = userRepository.findByRoleEntity_RoleName("ADMIN")
                .orElseThrow(() -> new AppException(ErrorCode.USER_ROLE_NOT_FOUND));
        Wallet adminWallet = walletRespository.findByUser(admin)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        // Kiểm tra số dư ví admin
        if (adminWallet.getBalance().compareTo(amount) < 0) {
            throw new AppException(ErrorCode.WALLET_BALANCE_NOT_ENOUGH);
        }

        // Lấy tổng số tiền user có thể rút từ Transactions
        BigDecimal availableEarnings = transactionsService.getEarningsSummary(userId).getTotalEarnings();
        if (availableEarnings == null || availableEarnings.compareTo(amount) < 0) {
            throw new AppException(ErrorCode.WALLET_BALANCE_NOT_ENOUGH);
        }

        // Tạo OTP
        String otp = emailService.generateOTP(user.getEmail());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime otpExpirationTime = now.plusMinutes(2);

        // Lưu thông tin rút tiền vào bảng Withdraws với trạng thái PENDING và OTP
        Withdraw withdraw = new Withdraw();
        withdraw.setUser(user);
        withdraw.setPrice(amount);
        withdraw.setRequestDate(now);
        withdraw.setFullname(user.getFullname());
        withdraw.setStatus(WithdrawStatus.PENDING);
        withdraw.setOtp(passwordEncoder.encode(otp));
        withdraw.setOtpCreationTime(now);
        withdraw.setOtpExpirationTime(otpExpirationTime);
        withdrawRepository.save(withdraw);

        // Gửi email chứa OTP
        try {
            emailService.sendOTPEmail(user.getEmail(), otp);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.EMAIL_SENDING_FAILED);
        }

        // Trả về WithdrawResponse
        WithdrawResponse response = new WithdrawResponse();
        response.setWithdrawId(withdraw.getId());
        response.setUserId(userId);
        response.setAmount(amount);
        response.setRequestDate(now);
        response.setStatus(WithdrawStatus.PENDING.name());
        return response;
    }

    @Transactional
    public WithdrawResponse confirmWithdraw(String userId, String otpInput, Long withdrawId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tìm yêu cầu rút tiền
        Withdraw withdraw = withdrawRepository.findById(withdrawId)
                .orElseThrow(() -> new AppException(ErrorCode.WITHDRAW_NOT_FOUND));

        // Kiểm tra xem yêu cầu rút tiền thuộc về user không
        if (!withdraw.getUser ().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Kiểm tra trạng thái yêu cầu
        if (!withdraw.getStatus().equals(WithdrawStatus.PENDING)) {
            throw new AppException(ErrorCode.WITHDRAW_ALREADY_PROCESSED);
        }

        // Kiểm tra thời gian hết hạn của OTP
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(withdraw.getOtpExpirationTime())) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        // Xác minh OTP
        boolean isOtpValid = emailService.verifyOTP(otpInput, withdraw.getOtp(),
                withdraw.getOtpCreationTime(), withdraw.getOtpExpirationTime());
        if (!isOtpValid) {
            throw new AppException(ErrorCode.OTP_VERIFICATION_FAILED);
        }

        // Lấy ví admin
        User admin = userRepository.findByRoleEntity_RoleName("ADMIN")
                .orElseThrow(() -> new AppException(ErrorCode.USER_ROLE_NOT_FOUND));
        Wallet adminWallet = walletRespository.findByUser (admin)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        BigDecimal amount = withdraw.getPrice();

        // Trừ tiền từ ví admin
        adminWallet.setBalance(adminWallet.getBalance().subtract(amount));
        adminWallet.setUpdateAt(LocalDateTime.now());
        walletRespository.save(adminWallet);

        // Ghi giao dịch rút cho user
        String transactionType = "INSTRUCTOR".equals(user.getRoleEntity().getRoleName()) ? "EARNING_WITHDRAWN" : "ADMIN_WITHDRAWN";
        Transactions userTransaction = new Transactions();
        userTransaction.setUser (user);
        userTransaction.setAmount(amount);
        userTransaction.setType(transactionType);
        userTransaction.setFullname(user.getFullname());
        userTransaction.setStatus(WithdrawStatus.COMPLETED);
        userTransaction.setCreatedAt(LocalDateTime.now());
        transactionRespository.save(userTransaction);

        // Cập nhật trạng thái yêu cầu rút tiền
        withdraw.setStatus(WithdrawStatus.COMPLETED);
        withdrawRepository.save(withdraw);

        // Gửi email xác nhận rút tiền thành công
        try {
            emailService.sendWithdrawConfirmationEmail(
                    user.getEmail(),
                    user.getFullname(),
                    amount,
                    withdraw.getRequestDate(),
                    withdraw.getStatus()
            );
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.EMAIL_SENDING_FAILED);
        }

        // Trả về phản hồi
        WithdrawResponse response = new WithdrawResponse();
        response.setWithdrawId(withdraw.getId());
        response.setUserId(userId);
        response.setAmount(amount);
        response.setRequestDate(withdraw.getRequestDate());
        response.setStatus(WithdrawStatus.COMPLETED.name());
        return response;
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void autoDeleteExpiredWithdrawals() {
        LocalDateTime now = LocalDateTime.now();
        List<Withdraw> expiredWithdrawals = withdrawRepository.findByStatusAndOtpExpirationTimeBefore(WithdrawStatus.PENDING, now);
        if (!expiredWithdrawals.isEmpty()) {
            withdrawRepository.deleteAll(expiredWithdrawals);
        }
    }

    @Override
    public List<WithdrawTransactionResponse> getUserWithdrawalHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy danh sách yêu cầu rút tiền của người dùng
        List<Withdraw> withdrawals = withdrawRepository.findByUser(user);

        return withdrawals.stream()
                .map(withdraw -> new WithdrawTransactionResponse(
                        withdraw.getId(),
                        withdraw.getPrice(),
                        withdraw.getRequestDate(),
                        withdraw.getStatus().name(),
                        withdraw.getUser ().getEmail(),
                        withdraw.getStatus(),
                        withdraw.getFullname()
                ))
                .collect(Collectors.toList());
    }
}
