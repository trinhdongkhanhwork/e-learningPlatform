package edu.cfd.e_learningPlatform.service.Impl;

import com.amazonaws.services.kms.model.NotFoundException;
import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawTransactionResponse;
import edu.cfd.e_learningPlatform.dto.response.WithdrawlSummaryResponse;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Map<String, List<WithdrawlSummaryResponse>> getWithdrawalSummary(String userId) {
        User user  = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Transactions> withdrawls = transactionRespository.findByUserAndTypeIn(user, List.of("EARNING_WITHDRAWN", "ADMIN_WITHDRAWN"));
        Map<String, List<WithdrawlSummaryResponse>> summary = new HashMap<>();

        summary.put("hour", summarizeWithdrawls(withdrawls, "hour"));
        summary.put("day", summarizeWithdrawls(withdrawls, "day"));
        summary.put("month", summarizeWithdrawls(withdrawls, "month"));
        summary.put("year", summarizeWithdrawls(withdrawls, "year"));

        return summary;
    }

    @Override
    public List<WithdrawlSummaryResponse> summarizeWithdrawls(List<Transactions> withdrawls, String timeFrame) {
        Map<String, BigDecimal> summaryMap = new HashMap<>();

        for (Transactions transaction : withdrawls) {
            LocalDateTime createdAt = transaction.getCreatedAt();
            String key;

            switch (timeFrame) {
                case "hour":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
                    break;
                case "day":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    break;
                case "month":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    break;
                case "year":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy"));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time frame: " + timeFrame);
            }

            // Cộng dồn số tiền vào summaryMap
            summaryMap.put(key, summaryMap.getOrDefault(key, BigDecimal.ZERO).add(transaction.getAmount()));
        }

        return summaryMap.entrySet().stream()
                .map(entry -> new WithdrawlSummaryResponse(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(WithdrawlSummaryResponse::getTimeFrame)) // Sắp xếp theo timeFrame
                .collect(Collectors.toList());
    }

    @Override
    public List<WithdrawlSummaryResponse> summarizeAllTransactions(String timeFrame) {
        // Lấy tất cả giao dịch
        List<Transactions> allTransactions = transactionRespository.findAll();

        // Lọc các giao dịch theo loại
        List<Transactions> filteredTransactions = allTransactions.stream()
                .filter(transaction ->
                    transaction.getType().equals("ADMIN_WITHDRAWN") ||
                    transaction.getType().equals("EARNING_WITHDRAWN"))
                .collect(Collectors.toList());

        // Gọi phương thức summarizeWithdrawls để tổng hợp các giao dịch đã lọc
        return summarizeWithdrawls(filteredTransactions, timeFrame);
    }


    @Override
    public List<WithdrawlSummaryResponse> summarizeWithdrawlsPayment(List<TransactionPayment> withdrawls, String timeFrame) {
        Map<String, BigDecimal> summaryMap = new HashMap<>();

        for (TransactionPayment transaction : withdrawls) {
            LocalDateTime createdAt = transaction.getCreatedAt();
            String key;

            switch (timeFrame) {
                case "hour":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
                    break;
                case "day":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    break;
                case "month":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    break;
                case "year":
                    key = createdAt.format(DateTimeFormatter.ofPattern("yyyy"));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time frame: " + timeFrame);
            }

            // Cộng dồn số tiền vào summaryMap
            summaryMap.put(key, summaryMap.getOrDefault(key, BigDecimal.ZERO).add(transaction.getAmount()));
        }
        return summaryMap.entrySet().stream()
                .map(entry -> new WithdrawlSummaryResponse(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(WithdrawlSummaryResponse::getTimeFrame)) // Sắp xếp theo timeFrame
                .collect(Collectors.toList());
    }


    @Override
    public List<WithdrawlSummaryResponse> summarizwAllTransactionPayments(String timeFrame) {
        List<TransactionPayment> allTransactionPayments = transactionPaymentRepository.findAll();

        List<TransactionPayment> filteredTransactionPayment = allTransactionPayments.stream()
                .filter(transactionPayment ->
                        transactionPayment.getType().equals("ADMIN_PROFIT") ||
                        transactionPayment.getType().equals("EARNING_PENDING"))
                .collect(Collectors.toList());

        return summarizeWithdrawlsPayment(filteredTransactionPayment,timeFrame);
    }

}

