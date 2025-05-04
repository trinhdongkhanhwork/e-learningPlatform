package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.*;
import edu.cfd.e_learningPlatform.dto.request.*;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.entity.*;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.mapstruct.CourseMapper;
import edu.cfd.e_learningPlatform.repository.*;
import edu.cfd.e_learningPlatform.service.CourseService;
import edu.cfd.e_learningPlatform.service.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PaymentRepository paymentRepository;
    private final CategoryRepository categoryRepository;
    private final SectionRepository sectionRepository;
    private final OptionRepository optionRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final LectureRepository lectureRepository;
    private final VideoRepository videoRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper, UserRepository userRepository, EmailService emailService, PaymentRepository paymentRepository, CategoryRepository categoryRepository, SectionRepository sectionRepository, OptionRepository optionRepository, QuizRepository quizRepository, QuestionRepository questionRepository, LectureRepository lectureRepository, VideoRepository videoRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.paymentRepository = paymentRepository;
        this.categoryRepository = categoryRepository;
        this.sectionRepository = sectionRepository;
        this.optionRepository = optionRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.lectureRepository = lectureRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public CourseResponse createCourse(CourseCreationRequest courseCreationRequest) {
        Course course = courseMapper.toCourse(courseCreationRequest);
        course.setInstructor(userRepository.findById(courseCreationRequest.getInstructor())
                .orElseThrow(() -> new RuntimeException("Instructor not found"))); // Thiết lập quan hệ giữa Course và User
//        course.setCategory(categoryRepository.findById(courseCreationRequest.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"))); // Thiết lập quan hệ giữa Course và Category

        course.setCategory(categoryRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Category not found")));
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
            long courseCountInCategory = categoryCourseCount.getOrDefault(course.getCategory().getId(), 0L);
            courseResponse.setCategoryCourseCount(courseCountInCategory);

            return courseResponse;
        });

        return courseResponses;
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Tính số lượng người đã ghi danh vào khóa học
        long enrolledCount = paymentRepository.countByCourseIdAndEnrollmentTrue(course.getId());

        CourseResponse courseResponse = courseMapper.toCourseResponse(course);
        courseResponse.setEnrolledUserCount(enrolledCount);

        return courseResponse;
    }

    @Override
    public CourseResponse updateCourse(Long courseId, CourseCreationRequest courseCreationRequest) {
        // Tìm Course theo ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Cập nhật các thuộc tính cơ bản của Course
        course.setTitle(courseCreationRequest.getTitle());
        course.setDescription(courseCreationRequest.getDescription());
        course.setCreatedAt(courseCreationRequest.getCreatedAt());
        course.setCategory(categoryRepository.findById(courseCreationRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        course.setCoverImage(courseCreationRequest.getCoverImage());
        course.setPrice(courseCreationRequest.getPrice());
        course.setPublished(false);
        course.setLevel(courseCreationRequest.getLevel());

        // Cập nhật Sections
        if (courseCreationRequest.getSections() != null) {
            List<Section> existingSections = course.getSections();
            List<Section> updatedSections = new ArrayList<>();

            // Tìm các section bị xóa
            Set<Long> sectionIds = courseCreationRequest.getSections().stream()
                    .map(SectionDto::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            List<Section> sectionsToRemove = existingSections.stream()
                    .filter(section -> !sectionIds.contains(section.getId()))
                    .collect(Collectors.toList());

            // Xóa các section bị xóa
            sectionsToRemove.forEach(sectionRepository::delete);

            for (SectionDto sectionRequest : courseCreationRequest.getSections()) {
                Section section = existingSections.stream()
                        .filter(existingSection -> existingSection.getId().equals(sectionRequest.getId()))
                        .findFirst()
                        .orElse(null); // Không tạo mới nếu không tìm thấy

                if (section != null) {
                    // Cập nhật các thuộc tính của Section
                    section.setTitle(sectionRequest.getTitle());
                    section.setCourse(course); // Đồng bộ hai chiều
                } else {
                    // Nếu không tìm thấy section, tạo mới
                    section = new Section();
                    section.setTitle(sectionRequest.getTitle());
                    section.setCourse(course); // Đồng bộ hai chiều
                }

                // Cập nhật Lectures
                if (sectionRequest.getLectures() != null) {
                    List<Lecture> existingLectures = section.getLectures();
                    List<Lecture> updatedLectures = new ArrayList<>();

                    // Tìm các lecture bị xóa
                    Set<Long> lectureIds = sectionRequest.getLectures().stream()
                            .map(LectureDto::getId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    List<Lecture> lecturesToRemove = existingLectures.stream()
                            .filter(lecture -> !lectureIds.contains(lecture.getId()))
                            .collect(Collectors.toList());

                    // Xóa các lecture bị xóa
                    lecturesToRemove.forEach(lectureRepository::delete);

                    for (LectureDto lectureRequest : sectionRequest.getLectures()) {
                        Lecture lecture = existingLectures.stream()
                                .filter(existingLecture -> existingLecture.getId().equals(lectureRequest.getId()))
                                .findFirst()
                                .orElse(null); // Không tạo mới nếu không tìm thấy

                        if (lecture != null) {
                            // Cập nhật các thuộc tính của Lecture
                            lecture.setTitle(lectureRequest.getTitle());
                            lecture.setType(lectureRequest.getType());
                            lecture.setSection(section); // Đồng bộ hai chiều
                        } else {
                            // Nếu không tìm thấy lecture, tạo mới
                            lecture = new Lecture();
                            lecture.setTitle(lectureRequest.getTitle());
                            lecture.setType(lectureRequest.getType());
                            lecture.setSection(section); // Đồng bộ hai chiều
                        }

                        // Cập nhật Videos, Quiz, Questions, Options, etc.
                        // Tương tự các đối tượng như Lecture, Section, giữ nguyên ID và chỉ cập nhật các thuộc tính
                        if (lectureRequest.getVideos() != null) {
                            List<Video> existingVideos = lecture.getVideos();
                            List<Video> updatedVideos = new ArrayList<>();

                            // Tìm các video bị xóa
                            Set<Long> videoIds = lectureRequest.getVideos().stream()
                                    .map(VideoDto::getId)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet());
                            List<Video> videosToRemove = existingVideos.stream()
                                    .filter(video -> !videoIds.contains(video.getId()))
                                    .collect(Collectors.toList());

                            // Xóa các video bị xóa
                            videosToRemove.forEach(videoRepository::delete);

                            for (VideoDto videoRequest : lectureRequest.getVideos()) {
                                Video video = existingVideos.stream()
                                        .filter(existingVideo -> existingVideo.getId().equals(videoRequest.getId()))
                                        .findFirst()
                                        .orElse(null); // Không tạo mới nếu không tìm thấy

                                if (video != null) {
                                    // Cập nhật các thuộc tính của Video
                                    video.setFileName(videoRequest.getFileName());
                                    video.setDuration(videoRequest.getDuration());
                                    video.setVideoUrl(videoRequest.getVideoUrl());
                                    video.setLecture(lecture); // Đồng bộ hai chiều
                                } else {
                                    // Nếu không tìm thấy video, tạo mới
                                    video = new Video();
                                    video.setFileName(videoRequest.getFileName());
                                    video.setDuration(videoRequest.getDuration());
                                    video.setVideoUrl(videoRequest.getVideoUrl());
                                    video.setLecture(lecture); // Đồng bộ hai chiều
                                }

                                updatedVideos.add(video);
                            }

                            existingVideos.clear();
                            existingVideos.addAll(updatedVideos);
                        }

                        // Cập nhật Quiz
                        if (lectureRequest.getQuiz() != null) {
                            Quiz quiz = lecture.getQuiz();
                            if (quiz == null) {
                                quiz = new Quiz();
                                lecture.setQuiz(quiz); // Liên kết mới nếu cần
                            }

                            quiz.setTitle(lectureRequest.getQuiz().getTitle());
                            quiz.setLecture(lecture); // Đồng bộ hai chiều

                            // Cập nhật Questions
                            if (lectureRequest.getQuiz().getQuestions() != null) {
                                List<Question> existingQuestions = quiz.getQuestions();
                                List<Question> updatedQuestions = new ArrayList<>();

                                // Tìm các question bị xóa
                                Set<Long> questionIds = lectureRequest.getQuiz().getQuestions().stream()
                                        .map(QuestionDto::getId)
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toSet());
                                List<Question> questionsToRemove = existingQuestions.stream()
                                        .filter(question -> !questionIds.contains(question.getId()))
                                        .collect(Collectors.toList());

                                // Xóa các question bị xóa
                                questionsToRemove.forEach(questionRepository::delete);

                                for (QuestionDto questionRequest : lectureRequest.getQuiz().getQuestions()) {
                                    Question question = existingQuestions.stream()
                                            .filter(existingQuestion -> existingQuestion.getId().equals(questionRequest.getId()))
                                            .findFirst()
                                            .orElse(null); // Không tạo mới nếu không tìm thấy

                                    if (question != null) {
                                        // Cập nhật các thuộc tính của Question
                                        question.setTitle(questionRequest.getTitle());
                                        question.setQuestionType(questionRequest.getType());
                                        question.setQuiz(quiz); // Đồng bộ hai chiều
                                    } else {
                                        // Nếu không tìm thấy question, tạo mới
                                        question = new Question();
                                        question.setTitle(questionRequest.getTitle());
                                        question.setQuestionType(questionRequest.getType());
                                        question.setQuiz(quiz); // Đồng bộ hai chiều
                                    }

                                    // Cập nhật Options
                                    if (questionRequest.getOptions() != null) {
                                        List<Option> existingOptions = question.getOptions();
                                        List<Option> updatedOptions = new ArrayList<>();

                                        // Tìm các option bị xóa
                                        Set<Long> optionIds = questionRequest.getOptions().stream()
                                                .map(OptionDto::getId)
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toSet());
                                        List<Option> optionsToRemove = existingOptions.stream()
                                                .filter(option -> !optionIds.contains(option.getId()))
                                                .collect(Collectors.toList());

                                        // Xóa các option bị xóa
                                        optionsToRemove.forEach(optionRepository::delete);

                                        for (OptionDto optionRequest : questionRequest.getOptions()) {
                                            Option option = existingOptions.stream()
                                                    .filter(existingOption -> existingOption.getId().equals(optionRequest.getId()))
                                                    .findFirst()
                                                    .orElse(null); // Không tạo mới nếu không tìm thấy

                                            if (option != null) {
                                                // Cập nhật các thuộc tính của Option
                                                option.setText(optionRequest.getText());
                                                option.setCorrect(optionRequest.isCorrect());
                                                option.setQuestion(question); // Đồng bộ hai chiều
                                            } else {
                                                // Nếu không tìm thấy option, tạo mới
                                                option = new Option();
                                                option.setText(optionRequest.getText());
                                                option.setCorrect(optionRequest.isCorrect());
                                                option.setQuestion(question); // Đồng bộ hai chiều
                                            }

                                            updatedOptions.add(option);
                                        }

                                        existingOptions.clear();
                                        existingOptions.addAll(updatedOptions);
                                    }

                                    updatedQuestions.add(question);
                                }

                                existingQuestions.clear();
                                existingQuestions.addAll(updatedQuestions);
                            }
                        }

                        updatedLectures.add(lecture);
                    }

                    existingLectures.clear();
                    existingLectures.addAll(updatedLectures);
                }

                updatedSections.add(section);
            }

            existingSections.clear();
            existingSections.addAll(updatedSections);
        }

        // Lưu toàn bộ thực thể được cập nhật với cascade
        course = courseRepository.save(course);

        // Trả về CourseResponse sau khi cập nhật
        return courseMapper.toCourseResponse(course);
    }


    @Override
    public void markForDeletion(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        course.setPublished(false);
        course.setPendingDelete(true);

        emailService.sendEmailDeleteCourse(id);

        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        //gửi mail thông báo đã xóa khóa học
        emailService.sendEmailDeleteCourse(id);
        //thực hiện xóa
        courseRepository.deleteById(id);
    }


    @Override
    public List<CourseResponse> getCoursesByCategoryId(Long categoryId) {
        // Kiểm tra category tồn tại
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Lấy danh sách khóa học theo categoryId
        List<Course> courses = courseRepository.findByCategoryId(categoryId);

        // Chuyển sang CourseResponse
        return courses.stream()
                .map(courseMapper::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalCourses() {
        return courseRepository.count();
    }

}
