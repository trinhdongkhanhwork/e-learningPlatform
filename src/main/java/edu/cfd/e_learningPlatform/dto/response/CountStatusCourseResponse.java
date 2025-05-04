package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountStatusCourseResponse {
    long publishedCourseCount;
    long draftCourseCount;
    long pendingCourseCount;
}
