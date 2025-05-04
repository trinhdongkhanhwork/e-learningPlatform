package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStatisticsDto {
    private String month;          // Tháng
    private Long totalCoursesSold; // Tổng số khóa học đã bán
    private Long totalBuyers;      // Tổng số người đã mua khóa học
    private Double totalRevenue;   // Tổng số tiền thu được
}
