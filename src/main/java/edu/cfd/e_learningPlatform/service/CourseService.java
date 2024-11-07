package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.CourseCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<CourseResponse> getAllCourses(int page, int size);
    CourseResponse getCourseById(Long id);
    CourseResponse createCourse(CourseCreationRequest courseCreationRequest);
    CourseResponse updateCourse(Long id, CourseCreationRequest courseCreationRequest);
    void deleteCourse(Long id);
}
