package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.ActivityLogRequest;
import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @PostMapping("/write")
    public ApiResponse<String> writeActivityLog(@RequestBody ActivityLogRequest request) {
        return activityLogService.logActivity(request);
    }

    @GetMapping("/read/{entityName}")
    public ApiResponse<List<String>> getAllActivityLogs(@PathVariable String entityName) {
        return activityLogService.getAllLogs(entityName);
    }
}
