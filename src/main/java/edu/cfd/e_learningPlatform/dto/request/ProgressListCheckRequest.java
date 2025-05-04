package edu.cfd.e_learningPlatform.dto.request;
import lombok.Data;

import java.util.List;

@Data
public class ProgressListCheckRequest {
    private String userId;
    private List<Long> lectureIds;
}
