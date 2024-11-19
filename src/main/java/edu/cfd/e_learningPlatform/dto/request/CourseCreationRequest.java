package edu.cfd.e_learningPlatform.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.cfd.e_learningPlatform.dto.SectionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreationRequest {
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String coverImage;
    private BigDecimal price;
    private boolean published;
    private String level;
    private String instructor;
    private List<SectionDto> sections = new ArrayList<>();
}
