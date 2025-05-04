package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.response.MonthlyTopBuyersDto;
import edu.cfd.e_learningPlatform.dto.response.RevenueStatisticsDto;
import edu.cfd.e_learningPlatform.dto.response.TopBuyerDto;
import edu.cfd.e_learningPlatform.dto.response.TopCourseDto;
import edu.cfd.e_learningPlatform.repository.RevenueRepository;
import edu.cfd.e_learningPlatform.service.RevenueService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RevenueServiceImpl implements RevenueService {
    private final RevenueRepository revenueRepository;

    public RevenueServiceImpl(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }

    @Override
    public List<MonthlyTopBuyersDto> getAllMonthsTopBuyers(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> rawData = revenueRepository.getAllMonthsWithTopBuyers(startDate, endDate);

        // Nhóm dữ liệu theo tháng
        Map<String, List<TopBuyerDto>> groupedData = new LinkedHashMap<>();
        for (Object[] row : rawData) {
            String monthName = (String) row[1];
            String userName = (String) row[4];
            Long totalCoursesBought = ((Number) row[2]).longValue();
            Double totalSpent = ((Number) row[3]).doubleValue();

            TopBuyerDto buyer = new TopBuyerDto(userName, totalCoursesBought, totalSpent);
            groupedData.computeIfAbsent(monthName, k -> new ArrayList<>()).add(buyer);
        }

        // Lấy top 5 người mua trong từng tháng
        List<MonthlyTopBuyersDto> result = new ArrayList<>();
        for (Map.Entry<String, List<TopBuyerDto>> entry : groupedData.entrySet()) {
            String monthName = entry.getKey();
            List<TopBuyerDto> top5Buyers = entry.getValue().stream()
                    .limit(5) // Chỉ lấy top 5 người mua
                    .collect(Collectors.toList());
            result.add(new MonthlyTopBuyersDto(monthName, top5Buyers));
        }

        return result;
    }

    @Override
    public List<RevenueStatisticsDto> getRevenueStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> rawData = revenueRepository.getRevenueStatistics(startDate, endDate);

        List<RevenueStatisticsDto> result = new ArrayList<>();
        for (Object[] row : rawData) {
            String monthName = (String) row[0];
            Long totalCoursesSold = ((Number) row[1]).longValue();
            Long totalUsers = ((Number) row[2]).longValue();
            Double totalRevenue = ((Number) row[3]).doubleValue();

            result.add(new RevenueStatisticsDto(monthName, totalCoursesSold, totalUsers, totalRevenue));
        }

        return result;
    }

    @Override
    public List<TopCourseDto> getTopCourses() {
        List<Object[]> rawData = revenueRepository.getTopCourses();

        List<TopCourseDto> result = new ArrayList<>();
        for (Object[] row : rawData) {
            Long courseId = ((Number) row[0]).longValue();
            String courseName = (String) row[1];
            Long totalSold = ((Number) row[2]).longValue();
            String imageUrl = (String) row[3];
            Double totalRevenue = ((Number) row[4]).doubleValue();

            result.add(new TopCourseDto(courseId ,courseName, totalSold, imageUrl, totalRevenue));
        }

        return result;
    }
}
