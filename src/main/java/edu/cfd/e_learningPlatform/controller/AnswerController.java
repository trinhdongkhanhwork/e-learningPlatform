package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.AnswerDto;
import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.service.Impl.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @GetMapping("/{lectureId}")
    public ApiResponse<List<AnswerDto>> getAnswers(@PathVariable Long lectureId) {
        List<AnswerDto> answers = answerService.getAnswersByUserAndLecture(lectureId);
        return ApiResponse.<List<AnswerDto>>builder().result(answers).build();
    }

    @PostMapping("/submit")
    public ApiResponse<AnswerDto> submitAnswer(@RequestBody AnswerDto answerDto) {
        AnswerDto savedAnswer = answerService.processAnswer(answerDto);
        return ApiResponse.<AnswerDto>builder().result(savedAnswer).build();
    }
}
