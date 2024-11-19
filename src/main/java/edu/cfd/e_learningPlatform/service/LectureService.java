package edu.cfd.e_learningPlatform.service;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.LectureDto;

public interface LectureService {
    LectureDto getLessonById(Long id);

    List<LectureDto> getAllLessons();

    LectureDto createLesson(LectureDto lectureDto);

    LectureDto updateLesson(Long id, LectureDto lectureDto);

    void deleteLesson(Long id);
}
