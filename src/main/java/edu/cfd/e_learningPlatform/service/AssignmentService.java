package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.AssignmentDto;

public interface AssignmentService {
    AssignmentDto findAssignmentInLecture(Long id);
}
