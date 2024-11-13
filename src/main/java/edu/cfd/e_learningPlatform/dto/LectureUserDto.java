package edu.cfd.e_learningPlatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureUserDto {
    private Long id;
    private String user;
    private Long lecture;
    private String choseOption;
    private String urlFileAssigment;
    private boolean doneQuiz;
    private boolean doneVideo;
    private boolean doneAssigment;
    private boolean status;
}