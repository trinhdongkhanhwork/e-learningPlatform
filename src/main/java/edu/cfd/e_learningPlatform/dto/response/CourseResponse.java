package edu.cfd.e_learningPlatform.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.cfd.e_learningPlatform.dto.SectionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String coverImage;
    private BigDecimal price;
    private boolean published;
    private String level;
    private UserResponse instructor;
    private List<SectionDto> sections = new ArrayList<>();
    private long enrolledUserCount;
    private long categoryCourseCount;
}
