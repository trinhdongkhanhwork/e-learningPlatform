package edu.cfd.e_learningPlatform.dto.response;

import edu.cfd.e_learningPlatform.dto.AssignmentDto;
import edu.cfd.e_learningPlatform.dto.QuizDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class LectueUserResponse {
    private Long id;
    private String title;
    private String type;
    private VideoInlectureResponse videoInlectureResponse;
    private QuizDto quiz;
    private AssignmentDto assignment;
    private List<Long> optionChose;
    private boolean status;
}