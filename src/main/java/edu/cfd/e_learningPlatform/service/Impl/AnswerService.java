package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.config.AuditorAwareImpl;
import edu.cfd.e_learningPlatform.dto.AnswerDto;
import edu.cfd.e_learningPlatform.dto.request.ProgressUpdateRequest;
import edu.cfd.e_learningPlatform.entity.Answer;
import edu.cfd.e_learningPlatform.entity.Lecture;
import edu.cfd.e_learningPlatform.entity.Option;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.mapstruct.AnswerMapper;
import edu.cfd.e_learningPlatform.repository.AnswerRepository;
import edu.cfd.e_learningPlatform.repository.LectureRepository;
import edu.cfd.e_learningPlatform.repository.OptionRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final AnswerMapper answerMapper;
    private final AuditorAwareImpl auditorAware;
    private final LearningProgressService learningProgressService;
    private final UserService userService;


    public List<AnswerDto> getAnswersByUserAndLecture(Long lectureId) {
        User user = userService.getCurrentUser();
        List<Answer> answers = answerRepository.findByUser_IdAndLecture_Id(user.getId(), lectureId);
        return answerMapper.toDtoList(answers);
    }

    @Transactional
    public AnswerDto processAnswer(AnswerDto answerDto) {
        User user = userService.getCurrentUser();

        List<Option> options = optionRepository.findByIdIn(answerDto.getOptionId());
        if (options.isEmpty()) {
            throw new RuntimeException("option not found");
        }

        long correctCount = options.stream().filter(Option::isCorrect).count();
        double correctPercentage = (double) correctCount / options.size();
        boolean passedLecture = correctPercentage >= 0.85;

        int totalScore = 100;
        int score = (int) (totalScore * correctPercentage);

        Lecture lecture = lectureRepository.findById(answerDto.getLectureId())
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        //  Gọi cập nhật tiến độ nếu đã vượt qua
        if (passedLecture) {
            ProgressUpdateRequest progressRequest = new ProgressUpdateRequest();
            progressRequest.setUserId(user.getId());
            progressRequest.setLectureId(lecture.getId());
            progressRequest.setCompleted(true);
            progressRequest.setCurrentSecond(0);

            learningProgressService.updateProgress(progressRequest);
        }
        Answer answer = Answer.builder()
                .lecture(lecture)
                .user(user)
                .score(score)
                .progressTimestamp(null)
                .passedLecture(passedLecture)
                .createdAt(LocalDateTime.now())
                .build();
        answerRepository.save(answer);


        answerDto.setUserId(user.getFullname());
        answerDto.setPassedLecture(passedLecture);
        answerDto.setScore(score);
        return answerDto;
    }
}