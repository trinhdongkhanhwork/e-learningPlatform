package edu.cfd.e_learningPlatform.service.Impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.CourseDto;
import edu.cfd.e_learningPlatform.dto.request.CourseCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.entity.*;
import edu.cfd.e_learningPlatform.mapstruct.CourseMapper;
import edu.cfd.e_learningPlatform.mapstruct.CourseMapperForLoad;
import edu.cfd.e_learningPlatform.repository.CategoryRepository;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.CourseService;
import edu.cfd.e_learningPlatform.service.EmailService;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PaymentRepository paymentRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    CourseMapperForLoad courseMapperForLoad;

    public CourseServiceImpl(
            CourseRepository courseRepository,
            CourseMapper courseMapper,
            UserRepository userRepository,
            EmailService emailService,
            PaymentRepository paymentRepository,
            CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.paymentRepository = paymentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CourseResponse createCourse(CourseCreationRequest courseCreationRequest) {
        Course course = courseMapper.toCourse(courseCreationRequest);
        course.setInstructor(userRepository
                .findById(courseCreationRequest.getInstructor())
                .orElseThrow(
                        () -> new RuntimeException("Instructor not found"))); // Thiết lập quan hệ giữa Course và User
        course.setCategory(categoryRepository
                .findById(courseCreationRequest.getCategoryId())
                .orElseThrow(
                        () -> new RuntimeException("Category not found"))); // Thiết lập quan hệ giữa Course và Category
        // Thiết lập quan hệ trước khi lưu
        if (course.getSections() != null) {
            for (Section section : course.getSections()) {
                section.setCourse(course); // Thiết lập quan hệ giữa Section và Course
                if (section.getLectures() != null) {
                    for (Lecture lecture : section.getLectures()) {
                        lecture.setSection(section); // Thiết lập quan hệ giữa Lecture và Section
                        if (lecture.getVideos() != null) {
                            for (Video video : lecture.getVideos()) {
                                video.setLecture(lecture); // Thiết lập quan hệ giữa Video và Lecture
                            }
                        }
                        if (lecture.getQuiz() != null) {
                            lecture.getQuiz().setLecture(lecture); // Thiết lập quan hệ giữa Quiz và Lecture
                            if (lecture.getQuiz().getQuestions() != null) {
                                for (Question question : lecture.getQuiz().getQuestions()) {
                                    question.setQuiz(lecture.getQuiz()); // Thiết lập quan hệ giữa Question và Quiz
                                    if (question.getOptions() != null) {
                                        for (Option option : question.getOptions()) {
                                            option.setQuestion(question); // Thiết lập quan hệ giữa Option và Question
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Lưu toàn bộ thực thể cùng lúc với cascade
        course = courseRepository.save(course);

        // Trả về CourseDto sau khi đã lưu
        return courseMapper.toCourseResponse(course);
    }

    @Override
    public Page<CourseResponse> getAllCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Lấy tất cả các khóa học trong trang
        Page<Course> coursePage = courseRepository.findAll(pageable);

        // Lấy danh sách tất cả các danh mục trong cơ sở dữ liệu
        List<Category> categories = categoryRepository.findAll();

        // Tính số lượng khóa học thuộc từng danh mục
        Map<Long, Long> categoryCourseCount = coursePage.getContent().stream()
                .collect(Collectors.groupingBy(course -> course.getCategory().getId(), Collectors.counting()));

        // Ánh xạ các khóa học sang CourseResponse và tính toán số lượng người đã đăng ký
        Page<CourseResponse> courseResponses = coursePage.map(course -> {
            CourseResponse courseResponse = courseMapper.toCourseResponse(course);

            // Lấy số lượng người đã đăng ký
            long enrolledCount = paymentRepository.countByCourseIdAndEnrollmentTrue(course.getId());
            courseResponse.setEnrolledUserCount(enrolledCount);

            // Thêm số lượng khóa học thuộc danh mục vào thông tin của mỗi khóa học
            long courseCountInCategory =
                    categoryCourseCount.getOrDefault(course.getCategory().getId(), 0L);
            courseResponse.setCategoryCourseCount(courseCountInCategory);

            return courseResponse;
        });

        return courseResponses;
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        // Tính số lượng người đã ghi danh vào khóa học
        long enrolledCount = paymentRepository.countByCourseIdAndEnrollmentTrue(course.getId());

        CourseResponse courseResponse = courseMapper.toCourseResponse(course);
        courseResponse.setEnrolledUserCount(enrolledCount);

        return courseResponse;
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseCreationRequest courseCreationRequest) {
        courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        Course course = courseMapper.toCourse(courseCreationRequest);
        return courseMapper.toCourseResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        emailService.sendEmailDeleteCourse(id);
        courseRepository.deleteById(id);
    }

    @Override
    public CourseDto getCourseByIdForLoad(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Khóa học không tồn tại với ID: " + courseId);
        }

        return courseMapperForLoad.toDto(courseOptional.get());
    }
}
