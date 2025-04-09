package edu.cfd.e_learningPlatform.dto.request;

import edu.cfd.e_learningPlatform.enums.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ActivityLogRequest {
    private String entityName;
    private ActivityType activity;
    private Map<String, String> data;
}
