package edu.cfd.e_learningPlatform.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponse {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private LocalDateTime issuedAt;
}
