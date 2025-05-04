package edu.cfd.e_learningPlatform.service;

import java.util.List;

public interface CloudWatchLogService {
    public void logMessage(String message);

    public List<String> getLogs();
}