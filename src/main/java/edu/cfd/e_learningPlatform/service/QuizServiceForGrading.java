package edu.cfd.e_learningPlatform.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.entity.Answer;
import edu.cfd.e_learningPlatform.entity.Option;
import edu.cfd.e_learningPlatform.repository.AnswerRepository;
import edu.cfd.e_learningPlatform.repository.OptionRepository;
import edu.cfd.e_learningPlatform.repository.QuizRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceForGrading {
    private final QuizRepository quizRepository;
    private final AnswerRepository answerRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    public int calculateScore(Long quizId, String userId, List<Long> selectedOptionIds) {
        // Lấy danh sách các option đúng của quiz
        List<Option> correctOptions = optionRepository.findCorrectOptionsByQuizId(quizId);

        // Tính số câu trả lời đúng
        int correctAnswers = 0;
        for (Option option : correctOptions) {
            if (selectedOptionIds.contains(option.getId())) {
                correctAnswers++;
            }
        }

        // Tính tổng số câu hỏi
        int totalQuestions = correctOptions.size();

        // Tính điểm theo % (mỗi câu trả lời đúng = 100 / tổng số câu hỏi)
        int score = (int) ((double) correctAnswers / totalQuestions * 100);

        // Lưu câu trả lời người dùng (nếu cần)
        saveUserAnswers(quizId, userId, selectedOptionIds, correctOptions);

        return score;
    }

    private void saveUserAnswers(
            Long quizId, String userId, List<Long> selectedOptionIds, List<Option> correctOptions) {
        for (Long selectedOptionId : selectedOptionIds) {
            Option selectedOption = optionRepository
                    .findById(selectedOptionId)
                    .orElseThrow(() -> new RuntimeException("Option not found"));

            Answer answer = new Answer();
            answer.setQuiz(quizRepository.findById(quizId).orElse(null));
            answer.setUser(userRepository.findById(userId).orElse(null));
            answer.setQuestion(selectedOption.getQuestion());
            answer.setSelectedOption(selectedOption);
            answer.setCorrect(correctOptions.contains(selectedOption));

            answerRepository.save(answer);
        }
    }
}
