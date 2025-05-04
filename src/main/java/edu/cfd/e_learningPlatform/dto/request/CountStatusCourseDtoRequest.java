package edu.cfd.e_learningPlatform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountStatusCourseDtoRequest {
    private long publishedCourseCount;
    private long pendingCourseCount;
    private long draftCourseCount;
}
