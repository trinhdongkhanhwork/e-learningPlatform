package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.CourseCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.dto.response.EnrollCourseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    Page<CourseResponse> getAllCourses(int page, int size);
    CourseResponse getCourseById(Long id);
    CourseResponse createCourse(CourseCreationRequest courseCreationRequest);
    CourseResponse updateCourse(Long id, CourseCreationRequest courseCreationRequest);
    void deleteCourse(Long id);
    List<EnrollCourseResponse> getEnrollCourses(String idStudent);
    List<EnrollCourseResponse> getCoursesIntructor(String idIntructor);
}
