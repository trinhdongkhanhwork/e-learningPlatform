package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.ChartRequestDto;
import edu.cfd.e_learningPlatform.dto.response.MonthlyTopBuyersDto;
import edu.cfd.e_learningPlatform.dto.response.RevenueStatisticsDto;
import edu.cfd.e_learningPlatform.dto.response.TopCourseDto;
import edu.cfd.e_learningPlatform.service.RevenueService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ChartDataController {
    private final RevenueService revenueService;

    public ChartDataController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    // Lấy dữ liệu thống kê doanh thu
    @GetMapping("/statistics")
    public List<RevenueStatisticsDto> getRevenueStatistics(@RequestParam(defaultValue = "") LocalDateTime startDate,
                                                            @RequestParam(defaultValue = "") LocalDateTime endDate) {
        System.out.println("Start date: " + startDate);
        System.out.println("End date: " + endDate);
         return revenueService.getRevenueStatistics(startDate, endDate);
    }

    @GetMapping("/topBuyers")
    public List<MonthlyTopBuyersDto> getTopBuyers(@Valid @RequestBody ChartRequestDto chartRequestDto) {
        return revenueService.getAllMonthsTopBuyers(chartRequestDto.getStartDate(), chartRequestDto.getEndDate());
    }

    @GetMapping("/topCourses")
    public List<TopCourseDto> getTopCourses() {
        return revenueService.getTopCourses();
    }
}
