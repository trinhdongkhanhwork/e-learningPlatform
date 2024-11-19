package edu.cfd.e_learningPlatform.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.request.ProgressUpdateRequest;
import edu.cfd.e_learningPlatform.entity.UserCourseProgress;
import edu.cfd.e_learningPlatform.entity.Video;
import edu.cfd.e_learningPlatform.repository.UserCourseProgressRepository;
import edu.cfd.e_learningPlatform.repository.VideoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCourseProgressService {

    private final UserCourseProgressRepository progressRepository;
    private final VideoRepository videoRepository;

    public void updateProgress(String userId, ProgressUpdateRequest request) {
        boolean isCompleted = determineCompletionStatus(request);

        UserCourseProgress existingProgress = progressRepository
                .findByUserIdAndCourseIdAndSectionIdAndLectureId(
                        userId, request.getCourseId(), request.getSectionId(), request.getLectureId())
                .orElse(null);

        if (existingProgress == null) {
            UserCourseProgress newProgress = new UserCourseProgress();
            newProgress.setUserId(userId);
            newProgress.setCourseId(request.getCourseId());
            newProgress.setSectionId(request.getSectionId());
            newProgress.setLectureId(request.getLectureId());
            newProgress.setTimeSpent(request.getTimeSpent());
            newProgress.setScore(request.getScore());
            newProgress.setCompleted(isCompleted);
            newProgress.setProgressTimestamp(LocalDateTime.now());
            progressRepository.save(newProgress);
        } else {
            existingProgress.setTimeSpent(request.getTimeSpent());
            existingProgress.setScore(request.getScore());
            existingProgress.setCompleted(isCompleted);
            existingProgress.setProgressTimestamp(LocalDateTime.now());
            progressRepository.save(existingProgress);
        }
    }

    private boolean determineCompletionStatus(ProgressUpdateRequest request) {
        Video video = videoRepository.findByLectureId(request.getLectureId()).orElse(null);

        if (video != null) {
            int videoDuration = Integer.parseInt(video.getDuration());
            return isVideoCompleted(request.getTimeSpent(), videoDuration);
        } else {
            return isQuizCompleted(request.getScore());
        }
    }

    public boolean isVideoCompleted(Integer timeSpent, int videoDuration) {
        return timeSpent != null && timeSpent >= 0.85 * videoDuration;
    }

    public boolean isQuizCompleted(Integer score) {
        return score != null && score >= 85;
    }
}
