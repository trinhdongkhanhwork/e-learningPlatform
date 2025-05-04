package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopBuyerDto {
    private String buyerName; // Tên người mua
    private Long totalCoursesBought; // Tổng số khóa học đã mua
    private Double totalSpent; // Tổng số tiền đã chi
}
