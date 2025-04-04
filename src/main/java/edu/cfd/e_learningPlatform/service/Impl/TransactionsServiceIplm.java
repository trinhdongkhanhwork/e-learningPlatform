package edu.cfd.e_learningPlatform.service.Impl;

import com.amazonaws.services.kms.model.NotFoundException;
import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import edu.cfd.e_learningPlatform.entity.TransactionPayment;
import edu.cfd.e_learningPlatform.entity.Transactions;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.repository.TransactionPaymentRepository;
import edu.cfd.e_learningPlatform.repository.TransactionRespository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.TransactionsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionsServiceIplm implements TransactionsService {

    TransactionPaymentRepository transactionPaymentRepository;
    TransactionRespository transactionRespository;
    UserRepository userRepository;

    @Override
    public EarningsSummaryResponse getEarningsSummary(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BigDecimal totalEarnings;
        String fullname = user.getFullname();
        String role = user.getRoleEntity().getRoleName();

        // Tính tổng số tiền đã rút từ Transactions
        BigDecimal totalWithdrawn = transactionRespository.findByUserAndTypeIn(user, List.of("EARNING_WITHDRAWN", "ADMIN_WITHDRAWN"))
                .stream()
                .map(Transactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính tổng số tiền đã cộng từ TransactionPayment
        BigDecimal totalAdded = transactionPaymentRepository.findByUserAndTypeIn(user, List.of("ADMIN_PROFIT", "EARNING_PENDING"))
                .stream()
                .map(TransactionPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính số tiền thực tế còn lại
        totalEarnings = totalAdded.subtract(totalWithdrawn);

        // Đảm bảo không trả về số âm
        if (totalEarnings.compareTo(BigDecimal.ZERO) < 0) {
            totalEarnings = BigDecimal.ZERO;
        }

        return new EarningsSummaryResponse(fullname, totalEarnings);
    }

    @Override
    public List<WithdrawTransactionResponse> getAllWithdrawals() {
        List<String> withdrawlType = List.of("EARNING_WITHDRAWN", "ADMIN_WITHDRAWN");

        List<Transactions> withdrawlTransactions = transactionRespository.findByTypeIn(withdrawlType);

        return withdrawlTransactions.stream()
                .map(tx -> new WithdrawTransactionResponse(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getCreatedAt(),
                        tx.getType(),
                        tx.getUser().getEmail(),
                        tx.getStatus(),
                        tx.getFullname()
                        ))
                        .collect(Collectors.toList());
    }

}

