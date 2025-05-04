package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.config.AuditorAwareImpl;
import edu.cfd.e_learningPlatform.dto.request.ActivityLogRequest;
import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.ActivityLogService;
import edu.cfd.e_learningPlatform.service.CloudWatchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.GetLogEventsResponse;
import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {
    private final CloudWatchLogService cloudWatchLogService;
    private final AuditorAwareImpl auditorAware;
    private final CloudWatchLogsClient logsClient;

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Value("${aws.cloudwatch.log-group}")
    private String logGroupName;

    @Value("${aws.cloudwatch.log-stream}")
    private String logStreamName;

    @Override
    public ApiResponse<String> logActivity(ActivityLogRequest request) {
        String username = auditorAware.getCurrentAuditor().orElse("Anonymous");

        String entityId = request.getData().values().iterator().next();
        String entityValue = getEntityNameById(request.getEntityName(), entityId);

        String logMessage = String.format(
                "%s %s %s %s  %s",
                LocalDateTime.now(),
                username,
                request.getActivity(),
                request.getEntityName(),
                entityValue
        );

        cloudWatchLogService.logMessage(logMessage);

        return ApiResponse.<String>builder()
                .message("Log hoạt động đã được ghi thành công vào CloudWatch.")
                .build();
    }

    private String getEntityNameById(String entityName, String entityId) {
        return switch (entityName) {
            case "User" -> userRepository.findById(entityId)
                    .map(User::getFullname).orElse("Unknown");
//            case "Course" -> courseRepository.findById(entityId)
//                    .map(Course::getTitle).orElse("Unknown");
//            case "Device" -> deviceRepository.findById(entityId)
//                    .map(Device::getSerialNumber).orElse("Unknown");
//            case "Location" -> locationRepository.findById(entityId)
//                    .map(Location::getLocationName).orElse("Unknown");
            default -> "Unknown";
        };
    }

    @Override
    public ApiResponse<List<String>> getAllLogs(String entityName) {
        GetLogEventsRequest request = GetLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .build();
        GetLogEventsResponse response = logsClient.getLogEvents(request);

        List<String> logs = response.events().stream()
                .map(OutputLogEvent::message)
                .filter(log -> log.contains(entityName))
                .collect(Collectors.toList());

        String message = logs.isEmpty() ? "Không tìm thấy log cho entity: " + entityName
                : "Danh sách log được lấy thành công cho entity: " + entityName;

        return ApiResponse.<List<String>>builder()
                .result(logs)
                .message(message)
                .build();
    }
}