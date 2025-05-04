package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.response.WalletResponse;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Wallet;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.TransactionPayment;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.mapstruct.WalletMapper;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.TransactionPaymentRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.repository.WalletRespository;
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
    PasswordEncoder passwordEncoder;

    static final BigDecimal INSTRUCTOR_SHARE = new BigDecimal("0.80");
    static final BigDecimal ADMIN_SHARE = new BigDecimal("0.20");

    @Transactional
    @Override
    public WalletResponse depositToAdminWallet(Long courseId, BigDecimal amount) {
        // Tìm người dùng có permission_id = 1
        User privilegedUser = userRepository.findAll().stream()
                .filter(user -> user.getPermissions().stream()
                        .anyMatch(permission -> permission.getId().equals(1L)))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy hoặc tạo ví cho người dùng đặc quyền
        Wallet privilegedWallet = walletRespository.findByUser(privilegedUser)
                .orElseGet(() -> {
                    Wallet wallet = new Wallet();
                    wallet.setUser(privilegedUser);
                    wallet.setRoleEntity(privilegedUser.getRoles().stream().findFirst()
                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
                    wallet.setBalance(BigDecimal.ZERO);
                    return walletRespository.save(wallet);
                });

        // Cộng toàn bộ số tiền vào ví người dùng đặc quyền
        privilegedWallet.setBalance(privilegedWallet.getBalance().add(amount));
        privilegedWallet.setUpdateAt(LocalDateTime.now());
        walletRespository.save(privilegedWallet);

        // Tính lợi nhuận admin (20%)
        BigDecimal adminProfit = amount.multiply(ADMIN_SHARE);

        // Ghi giao dịch cho người dùng đặc quyền
        TransactionPayment privilegedTransaction = new TransactionPayment();
        privilegedTransaction.setUser(privilegedUser);
        privilegedTransaction.setAmount(adminProfit);
        privilegedTransaction.setType("ADMIN_PROFIT");
        privilegedTransaction.setFullname(privilegedUser.getFullname());
        privilegedTransaction.setCreatedAt(LocalDateTime.now());
        transactionRespository.save(privilegedTransaction);

        // Tìm khóa học
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        User instructor = course.getInstructor();

        if (instructor == null) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND);
        }

        // Tính lợi nhuận giảng viên (80%)
        BigDecimal instructorProfit = amount.multiply(INSTRUCTOR_SHARE);

        // Ghi giao dịch cho giảng viên
        TransactionPayment instructorTransaction = new TransactionPayment();
        instructorTransaction.setUser(instructor);
        instructorTransaction.setAmount(instructorProfit);
        instructorTransaction.setType("EARNING_PENDING");
        instructorTransaction.setFullname(instructor.getFullname());
        instructorTransaction.setCreatedAt(LocalDateTime.now());
        transactionRespository.save(instructorTransaction);

        return walletMapper.toWalletResponse(privilegedWallet);
    }

    @Override
    public WalletResponse getWalletByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Wallet wallet = walletRespository.findByUser(user)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUser(user);
                    newWallet.setRoleEntity(user.getRoles().stream().findFirst()
                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
                    newWallet.setBalance(BigDecimal.ZERO);
                    newWallet.setUpdateAt(LocalDateTime.now());
                    return walletRespository.save(newWallet);
                });
        return walletMapper.toWalletResponse(wallet);
    }
}