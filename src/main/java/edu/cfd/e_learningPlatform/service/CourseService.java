package edu.cfd.e_learningPlatform.service;

import org.springframework.data.domain.Page;

import edu.cfd.e_learningPlatform.dto.CourseDto;
import edu.cfd.e_learningPlatform.dto.request.CourseCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;

public interface CourseService {
    Page<CourseResponse> getAllCourses(int page, int size);

    CourseResponse getCourseById(Long id);

    CourseResponse createCourse(CourseCreationRequest courseCreationRequest);

    CourseResponse updateCourse(Long id, CourseCreationRequest courseCreationRequest);

    void markForDeletion(Long id);

    void deleteCourse(Long id);

}