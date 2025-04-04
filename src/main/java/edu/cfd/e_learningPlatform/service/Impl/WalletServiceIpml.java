package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.response.WalletResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawResponse;
import edu.cfd.e_learningPlatform.entity.*;
import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.mapstruct.WalletMapper;
import edu.cfd.e_learningPlatform.repository.*;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.TransactionsService;
import edu.cfd.e_learningPlatform.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletServiceIpml implements WalletService {

    WalletRespository walletRespository;
    UserRepository userRepository;
    CourseRepository courseRepository;
    TransactionPaymentRepository transactionRespository;
    WalletMapper walletMapper;

    static final BigDecimal ADMIN_SHARE = new BigDecimal("0.20");
    static final BigDecimal INSTRUCTOR_SHARE = new BigDecimal("0.80");
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public WalletResponse depositToAdminWallet(Long courseId, BigDecimal amount) {
        User admin = userRepository.findByRoleEntity_RoleName("ADMIN")
                .orElseThrow(() -> new AppException(ErrorCode.USER_ROLE_NOT_FOUND));

        // Lấy ví của admin
        Wallet adminWallet = walletRespository.findByUser(admin)
                .orElseGet(() -> {
                    Wallet wallet = new Wallet();
                    wallet.setUser(admin);
                    wallet.setRoleEntity(admin.getRoleEntity());
                    wallet.setBalance(BigDecimal.ZERO);
                    return walletRespository.save(wallet);
                });

        // Cộng toàn bộ số tiền vào ví admin
        adminWallet.setBalance(adminWallet.getBalance().add(amount));
        adminWallet.setUpdateAt(LocalDateTime.now());
        walletRespository.save(adminWallet);

        // Tính lợi nhuận của admin (20%)
        BigDecimal adminProfit = amount.multiply(ADMIN_SHARE);
        // Ghi giao dịch cho admin
        TransactionPayment adminTransaction = new TransactionPayment();
        adminTransaction.setUser(admin);
        adminTransaction.setAmount(adminProfit);
        adminTransaction.setType("ADMIN_PROFIT");
        adminTransaction.setFullname(admin.getFullname());
        adminTransaction.setCreatedAt(LocalDateTime.now());
        transactionRespository.save(adminTransaction);

        // Tìm instructor của khóa học
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        User instructor = course.getInstructor();

        // Tính lợi nhuận của instructor (80%)
        BigDecimal instructorProfit = amount.multiply(INSTRUCTOR_SHARE);
        // Ghi giao dịch cho instructor
        TransactionPayment instructorTransaction = new TransactionPayment();
        instructorTransaction.setUser(instructor);
        instructorTransaction.setAmount(instructorProfit);
        instructorTransaction.setType("EARNING_PENDING");
        instructorTransaction.setFullname(instructor.getFullname());
        instructorTransaction.setCreatedAt(LocalDateTime.now());
        transactionRespository.save(instructorTransaction);

        return walletMapper.toWalletResponse(adminWallet);
    }
    @Override
    public WalletResponse getWalletByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Wallet wallet = walletRespository.findByUser(user)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUser(user);
                    newWallet.setRoleEntity(user.getRoleEntity());
                    newWallet.setBalance(BigDecimal.ZERO);
                    newWallet.setUpdateAt(LocalDateTime.now());
                    return walletRespository.save(newWallet);
                });
        return walletMapper.toWalletResponse(wallet);
    }

}
