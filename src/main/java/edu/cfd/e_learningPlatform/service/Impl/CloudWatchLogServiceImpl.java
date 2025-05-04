package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.service.CloudWatchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CloudWatchLogServiceImpl implements CloudWatchLogService {

    private final CloudWatchLogsClient logsClient;

    @Value("${aws.cloudwatch.log-group}")
    private String logGroupName;

    @Value("${aws.cloudwatch.log-stream}")
    private String logStreamName;

    @Override
    public void logMessage(String message) {
        PutLogEventsRequest request = PutLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .logEvents(InputLogEvent.builder()
                        .message(message)
                        .timestamp(System.currentTimeMillis())
                        .build())
                .build();
        logsClient.putLogEvents(request);
    }

    @Override
    public List<String> getLogs() {
        GetLogEventsRequest request = GetLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStreamName)
                .build();
        GetLogEventsResponse response = logsClient.getLogEvents(request);
        return response.events().stream()
                .map(OutputLogEvent::message)
                .collect(Collectors.toList());
    }
}