package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.response.MonthlyTopBuyersDto;
import edu.cfd.e_learningPlatform.dto.response.RevenueStatisticsDto;
import edu.cfd.e_learningPlatform.dto.response.TopCourseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenueService {

    /**
     * Lấy danh sách người mua hàng đầu theo tháng
     *
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Danh sách người mua hàng đầu theo tháng
     */
    List<MonthlyTopBuyersDto> getAllMonthsTopBuyers(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Lấy thống kê doanh thu
     *
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Danh sách thống kê doanh thu
     */
    List<RevenueStatisticsDto> getRevenueStatistics(LocalDateTime startDate, LocalDateTime endDate);


    /**
     * Lấy danh sách khóa học bán chạy nhất
     *
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Danh sách khóa học bán chạy nhất
     */
    List<TopCourseDto> getTopCourses();
}
