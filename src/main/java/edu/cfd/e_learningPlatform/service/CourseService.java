package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.CourseCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.CountStatusCourseResponse;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    Page<CourseResponse> getAllCourses(int page, int size);

    CourseResponse getCourseById(Long id);

    CourseResponse createCourse(CourseCreationRequest courseCreationRequest);

    CourseResponse updateCourse(Long id, CourseCreationRequest courseCreationRequest);

    void markForDeletion(Long id);

    void deleteCourse(Long id);

    List<CourseResponse> getCoursesByCategoryId(Long categoryId);

    CountStatusCourseResponse countStatusCourse();

    long getTotalCourses();
}
