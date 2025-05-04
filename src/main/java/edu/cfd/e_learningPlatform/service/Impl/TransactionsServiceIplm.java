package edu.cfd.e_learningPlatform.service.Impl;

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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    //lịch sử rút tiền theo user
    @Override
    public EarningsSummaryResponse getEarningsSummary(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BigDecimal totalEarnings;
        String fullname = user.getFullname();

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

    //All lịch sử rút tiền
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

    //thống kê rút tiền theo user
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

    //Transactions
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

    //thống kê rút tiền
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


    //Thống kê tiền vào theo user
    @Override
    public Map<String, List<WithdrawlSummaryResponse>> getWithdrawSummaryPayment(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        List<TransactionPayment> withdrawlPayment = transactionPaymentRepository.findByUserAndTypeIn(user, List.of("ADMIN_PROFIT","EARNING_PENDING"));
        Map<String, List<WithdrawlSummaryResponse>> summary = new HashMap<>();

        summary.put("hour", summarizeWithdrawlsPayment(withdrawlPayment, "hour"));
        summary.put("day", summarizeWithdrawlsPayment(withdrawlPayment, "day"));
        summary.put("month", summarizeWithdrawlsPayment(withdrawlPayment, "month"));
        summary.put("year", summarizeWithdrawlsPayment(withdrawlPayment, "year"));

        return summary;
    }

    //Transaction Payment
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

    //thống kê tiền vào ví
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

    //xuất excel lịch sử rút tiền của user
    @Override
    public byte[] exportWithdrawlHistoryToExcel(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Transactions> withdrawlTransactions = transactionRespository.findByUserAndTypeInAndCreatedAtBetween(
                user,
                List.of("EARNING_WITHDRAWN", "ADMIN_WITHDRAWN"),
                startDate,
                endDate
        );

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()){

            //tạo sheet Excel
            Sheet sheet = workbook.createSheet("Lịch sử rút tiền");

            //Tạo header cho file
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Họ tên", "Trạng thái","Ngày giao dịch", "Số tiền"};

            //tạo style cho header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Bold style for total row
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            //tạo các header cell
            for (int i =0; i< columns.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Calculate total amount
            BigDecimal totalAmount = withdrawlTransactions.stream()
                    .map(Transactions::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //format date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            //format currency
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat df = workbook.createDataFormat();
            currencyStyle.setDataFormat(df.getFormat("###,### $"));

            // Bold currency style for total amount
            CellStyle boldCurrencyStyle = workbook.createCellStyle();
            boldCurrencyStyle.setDataFormat(df.getFormat("###,### $"));
            boldCurrencyStyle.setFont(boldFont);

            //điền dữ liệu
            int rowNum = 1;
            for (Transactions transactions : withdrawlTransactions){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(transactions.getId());
                row.createCell(1).setCellValue(transactions.getFullname());
                row.createCell(2).setCellValue(String.valueOf(transactions.getStatus()));
                row.createCell(3).setCellValue(transactions.getCreatedAt().format(formatter));

                Cell amountCell = row.createCell(4);
                amountCell.setCellValue(transactions.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);
            }

            // Add total row
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(3);
            totalLabelCell.setCellValue("Tổng cộng:");
            totalLabelCell.setCellStyle(boldStyle);

            Cell totalAmountCell = totalRow.createCell(4);
            totalAmountCell.setCellValue(totalAmount.doubleValue());
            totalAmountCell.setCellStyle(boldCurrencyStyle);

            //auto size cho các cột
            for (int i = 0; i< columns.length; i++){
                sheet.autoSizeColumn(i);
            }

            //ghi workbook ra byteArrayOutputStream
            workbook.write(out);
            return out.toByteArray();
        }catch (IOException e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] exportMoneyHistoryToExcel(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        List<TransactionPayment> withdrawlTransactions = transactionPaymentRepository.findByUserAndTypeInAndCreatedAtBetween(
                user,
                List.of("ADMIN_PROFIT", "EARNING_PENDING"),
                startDate,
                endDate
        );

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()){

            Sheet sheet = workbook.createSheet("Lịch sử tiền nạp ví");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Họ tên","Ngày giao dịch", "Số tiền"};

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            //tạo các header cell
            for (int i =0; i< columns.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            //format date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            //format currency
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat df = workbook.createDataFormat();
            currencyStyle.setDataFormat(df.getFormat("###,### $"));

            // Bold style for total row
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            // Bold currency style for total amount
            CellStyle boldCurrencyStyle = workbook.createCellStyle();
            boldCurrencyStyle.setDataFormat(df.getFormat("###,### $"));
            boldCurrencyStyle.setFont(boldFont);

            // Calculate total amount
            BigDecimal totalAmount = withdrawlTransactions.stream()
                    .map(TransactionPayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //điền dữ liệu
            int rowNum = 1;
            for (TransactionPayment transactions : withdrawlTransactions){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(transactions.getId());
                row.createCell(1).setCellValue(transactions.getFullname());
                row.createCell(2).setCellValue(transactions.getCreatedAt().format(formatter));

                Cell amountCell = row.createCell(3);
                amountCell.setCellValue(transactions.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);
            }

            // Add total row
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(3);
            totalLabelCell.setCellValue("Tổng cộng:");
            totalLabelCell.setCellStyle(boldStyle);

            Cell totalAmountCell = totalRow.createCell(4);
            totalAmountCell.setCellValue(totalAmount.doubleValue());
            totalAmountCell.setCellStyle(boldCurrencyStyle);

            //auto size cho các cột
            for (int i = 0; i< columns.length; i++){
                sheet.autoSizeColumn(i);
            }

            //ghi workbook ra byteArrayOutputStream
            workbook.write(out);
            return out.toByteArray();
        }catch (IOException e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] exportAllTransactionToExcel(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transactions> allTransaction = transactionRespository.findByCreatedAtBetween(startDate,endDate);

        try(Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            Sheet sheet = workbook.createSheet("Lịch sử rút tiền");

            //Tạo header cho file
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Họ tên", "Trạng thái","Ngày giao dịch", "Số tiền"};

            //tạo style cho header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Bold style for total row
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            //tạo các header cell
            for (int i =0; i< columns.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            //format date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            //format currency
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat df = workbook.createDataFormat();
            currencyStyle.setDataFormat(df.getFormat("###,### $"));

            //format boldCurrencyStyle
            CellStyle boldCurrencyStyle = workbook.createCellStyle();
            boldCurrencyStyle.setDataFormat(df.getFormat("###,### $"));
            boldCurrencyStyle.setFont(boldFont);

            // Calculate total amount
            BigDecimal totalAmount = allTransaction.stream()
                    .map(Transactions::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //điền dữ liệu
            int rowNum = 1;
            for (Transactions transactions : allTransaction){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(transactions.getId());
                row.createCell(1).setCellValue(transactions.getFullname());
                row.createCell(2).setCellValue(String.valueOf(transactions.getStatus()));
                row.createCell(3).setCellValue(transactions.getCreatedAt().format(formatter));

                Cell amountCell = row.createCell(4);
                amountCell.setCellValue(transactions.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);
            }

            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(3);
            totalLabelCell.setCellValue("Tổng cộng:");
            totalLabelCell.setCellStyle(boldStyle);

            Cell totalAmountCell = totalRow.createCell(4);
            totalAmountCell.setCellValue(totalAmount.doubleValue());
            totalAmountCell.setCellStyle(boldCurrencyStyle);

            //auto size cho các cột
            for (int i = 0; i< columns.length; i++){
                sheet.autoSizeColumn(i);
            }

            //ghi workbook ra byteArrayOutputStream
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }catch (IOException e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] exportAllTransactionPaymentToExcel(LocalDateTime startDate, LocalDateTime endDate) {
        List<TransactionPayment> allTransaction = transactionPaymentRepository.findByCreatedAtBetween(startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Lịch sử rút tiền");

            //Tạo header cho file
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Họ tên", "Ngày giao dịch", "Số tiền"};

            //tạo style cho header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Bold style for total row
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            //tạo các header cell
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            //format date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            //format currency
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat df = workbook.createDataFormat();
            currencyStyle.setDataFormat(df.getFormat("###,### $"));

            //format boldCurrencyStyle
            CellStyle boldCurrencyStyle = workbook.createCellStyle();
            boldCurrencyStyle.setDataFormat(df.getFormat("###,### $"));
            boldCurrencyStyle.setFont(boldFont);

            // Calculate total amount
            BigDecimal totalAmount = allTransaction.stream()
                    .map(TransactionPayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //điền dữ liệu
            int rowNum = 1;
            for (TransactionPayment transactions : allTransaction) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(transactions.getId());
                row.createCell(1).setCellValue(transactions.getFullname());
                row.createCell(2).setCellValue(transactions.getCreatedAt().format(formatter));

                Cell amountCell = row.createCell(3);
                amountCell.setCellValue(transactions.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);
            }

            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(2);
            totalLabelCell.setCellValue("Tổng cộng:");
            totalLabelCell.setCellStyle(boldStyle);

            Cell totalAmountCell = totalRow.createCell(3);
            totalAmountCell.setCellValue(totalAmount.doubleValue());
            totalAmountCell.setCellStyle(boldCurrencyStyle);

            //auto size cho các cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            //ghi workbook ra byteArrayOutputStream
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


}

