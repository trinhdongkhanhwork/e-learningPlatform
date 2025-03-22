package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String coverImage;
    private BigDecimal price;
    private String level;
    private boolean isPublished;
    private Long categoryId;
    private Long instructorId;
    private List<SectionDto> sections;
}
