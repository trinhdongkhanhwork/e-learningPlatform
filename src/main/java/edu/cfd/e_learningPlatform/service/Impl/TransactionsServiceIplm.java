package edu.cfd.e_learningPlatform.service.Impl;

import com.amazonaws.services.kms.model.NotFoundException;
import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.entity.Transactions;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.repository.TransactionRespository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.TransactionsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionsServiceIplm implements TransactionsService {

    TransactionRespository transactionRespository;
    UserRepository userRepository;

    @Override
    public EarningsSummaryResponse getEarningsSummary(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BigDecimal totalEarnings;
        String role = user.getRoleEntity().getRoleName();

        if ("INSTRUCTOR".equals(role)) {
            // Tính tổng số tiền từ EARNING_PENDING
            BigDecimal pendingEarnings = transactionRespository.findByUserAndType(user, "EARNING_PENDING")
                    .stream()
                    .map(Transactions::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Tính tổng số tiền đã rút (EARNING_WITHDRAWN)
            BigDecimal withdrawnEarnings = transactionRespository.findByUserAndType(user, "EARNING_WITHDRAWN")
                    .stream()
                    .map(Transactions::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Tổng số tiền thực tế còn lại = pending - withdrawn
            totalEarnings = pendingEarnings.subtract(withdrawnEarnings);

            // Đảm bảo không trả về số âm
            if (totalEarnings.compareTo(BigDecimal.ZERO) < 0) {
                totalEarnings = BigDecimal.ZERO;
            }
        } else if ("ADMIN".equals(role)) {
            BigDecimal totalProfit = transactionRespository.findByUserAndType(user, "ADMIN_PROFIT")
                    .stream()
                    .map(Transactions::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalWithdrawn = transactionRespository.findByUserAndType(user, "ADMIN_WITHDRAWN")
                    .stream()
                    .map(Transactions::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalEarnings = totalProfit.subtract(totalWithdrawn);
        } else {
            totalEarnings = BigDecimal.ZERO; // Role khác không được rút
        }

        return new EarningsSummaryResponse(userId, totalEarnings);
    }
}

