package edu.cfd.e_learningPlatform.dto.response;
import edu.cfd.e_learningPlatform.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponse {
    private Long id;
    private CourseResponse CourseResponse;
    private LocalDateTime issuedAt;
}
