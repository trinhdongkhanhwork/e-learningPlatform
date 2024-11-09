package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.LectureDto;
import edu.cfd.e_learningPlatform.dto.response.LectueUserResponse;
import edu.cfd.e_learningPlatform.entity.Lecture;
import edu.cfd.e_learningPlatform.mapstruct.LectureMapper;
import edu.cfd.e_learningPlatform.repository.LectureRepository;
import edu.cfd.e_learningPlatform.service.AssignmentService;
import edu.cfd.e_learningPlatform.service.LectureService;
import edu.cfd.e_learningPlatform.service.QuizService;
import edu.cfd.e_learningPlatform.service.VideoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final VideoService videoService;
    private final QuizService quizService;
    private final AssignmentService assignmentService;
    private final LectureMapper lectureMapper = LectureMapper.INSTANCE;

    public LectureServiceImpl(LectureRepository lectureRepository, VideoService videoService, QuizService quizService, AssignmentService assignmentService) {
        this.lectureRepository = lectureRepository;
        this.videoService = videoService;
        this.quizService = quizService;
        this.assignmentService = assignmentService;
    }

    @Override
    public LectueUserResponse getLectureUserById(Long lectureId) {
        LectueUserResponse lectueUserResponse = lectureMapper.lectureToLectueUserResponse(
                lectureRepository.findById(lectureId).orElseThrow(() -> new RuntimeException("Not found Lecture"))
        );
        lectueUserResponse.setVideoInlectureResponse(videoService.getVideoLecture(lectureId));
        lectueUserResponse.setQuiz(quizService.findByLectureId(lectureId));
        lectueUserResponse.setAssignment(assignmentService.findAssignmentInLecture(lectureId));
        return lectueUserResponse;
    }

    @Override
    public LectureDto getLessonById(Long id) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("Lecture not found"));
        return lectureMapper.lectureToLectureDto(lecture);
    }

    @Override
    public List<LectureDto> getAllLessons() {
        return lectureRepository.findAll().stream()
                .map(lectureMapper::lectureToLectureDto)
                .collect(Collectors.toList());
    }

    @Override
    public LectureDto createLesson(LectureDto lectureDto) {
        Lecture lecture = lectureMapper.lectureDtoToLecture(lectureDto);
        lecture.setSection(lectureMapper.lectureDtoToLecture(lectureDto).getSection());
        lecture.setVideos(lectureMapper.lectureDtoToLecture(lectureDto).getVideos());
        lecture.setQuiz(lectureMapper.lectureDtoToLecture(lectureDto).getQuiz());
        lecture = lectureRepository.save(lecture);
        return lectureMapper.lectureToLectureDto(lecture);
    }

    @Override
    public LectureDto updateLesson(Long id, LectureDto lectureDto) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("Lecture not found"));
        lecture.setTitle(lectureDto.getTitle());
        lecture = lectureRepository.save(lecture);
        return lectureMapper.lectureToLectureDto(lecture);
    }

    @Override
    public void deleteLesson(Long id) {
        lectureRepository.deleteById(id);
    }
}
