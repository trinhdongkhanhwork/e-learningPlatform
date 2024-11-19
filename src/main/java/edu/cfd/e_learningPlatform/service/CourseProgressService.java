package edu.cfd.e_learningPlatform.service;

import java.util.*;

import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.response.CourseProgressResponse;
import edu.cfd.e_learningPlatform.entity.UserCourseProgress;
import edu.cfd.e_learningPlatform.repository.UserCourseProgressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseProgressService {

    private final UserCourseProgressRepository progressRepository;

    public CourseProgressResponse getCourseProgress(String userId, Long courseId) {
        if (userId == null || courseId == null) {
            throw new IllegalArgumentException("Invalid userId or courseId.");
        }

        // Tổng số bài giảng trong khóa học
        long totalLectures = progressRepository.countTotalLecturesByCourseId(courseId);

        // Số bài giảng đã hoàn thành của người dùng
        long completedLectures = progressRepository.countCompletedLecturesByUserAndCourse(userId, courseId);

        // Lấy danh sách tiến trình học của người dùng
        List<UserCourseProgress> progressList = progressRepository.findByUserIdAndCourseId(userId, courseId);

        // Phân nhóm các bài giảng theo section
        Map<Long, CourseProgressResponse.SectionProgress> sectionMap = new HashMap<>();
        for (UserCourseProgress progress : progressList) {
            sectionMap
                    .computeIfAbsent(
                            progress.getSectionId(),
                            sectionId -> new CourseProgressResponse.SectionProgress(sectionId, new ArrayList<>()))
                    .getLectures()
                    .add(new CourseProgressResponse.LectureProgress(
                            progress.getLectureId(),
                            progress.getCompleted(),
                            progress.getTimeSpent(),
                            progress.getScore()));
        }

        // Tính phần trăm hoàn thành
        double completionPercentage = totalLectures == 0 ? 0 : (double) completedLectures / totalLectures * 100;

        // Chuẩn bị phản hồi
        return CourseProgressResponse.builder()
                .courseId(courseId)
                .completedLectures(completedLectures)
                .totalLectures(totalLectures)
                .completionPercentage(completionPercentage)
                .sections(new ArrayList<>(sectionMap.values()))
                .build();
    }
}
