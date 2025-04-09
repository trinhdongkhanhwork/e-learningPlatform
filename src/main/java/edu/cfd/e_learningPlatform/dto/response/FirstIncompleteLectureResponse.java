package edu.cfd.e_learningPlatform.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstIncompleteLectureResponse {
    private Long lectureId;
    private Integer currentSecond;
    private int totalCompleted;
    private int totalLectures;
    private boolean eligibleForCertificate;
}