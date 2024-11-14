package edu.cfd.e_learningPlatform.service;


import edu.cfd.e_learningPlatform.dto.LectureDto;
import edu.cfd.e_learningPlatform.dto.response.LectueUserResponse;

import java.util.List;

public interface LectureService {
    LectueUserResponse getLectureUserById(Long lectureId);
    LectureDto getLessonById(Long id);
    List<LectureDto> getAllLessons();
    LectureDto createLesson(LectureDto lectureDto);
    LectureDto updateLesson(Long id, LectureDto lectureDto);
    void deleteLesson(Long id);
}
