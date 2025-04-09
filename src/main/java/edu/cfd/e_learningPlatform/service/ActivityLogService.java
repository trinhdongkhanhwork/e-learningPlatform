package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.ActivityLogRequest;
import edu.cfd.e_learningPlatform.dto.response.ApiResponse;

import java.util.List;

public interface ActivityLogService {
    ApiResponse<String> logActivity(ActivityLogRequest request);

    ApiResponse<List<String>> getAllLogs(String entityName);
}
