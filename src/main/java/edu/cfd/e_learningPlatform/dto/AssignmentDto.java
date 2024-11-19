package edu.cfd.e_learningPlatform.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDto {
    private int id;
    private String title;
    private String description;
    private String assignmentFileUrl;
    private Date dueDate;
}
