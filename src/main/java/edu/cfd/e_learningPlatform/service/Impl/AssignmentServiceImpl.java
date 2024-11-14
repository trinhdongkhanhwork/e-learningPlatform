package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.AssignmentDto;
import edu.cfd.e_learningPlatform.mapstruct.AssignmentMapper;
import edu.cfd.e_learningPlatform.repository.AssignmentRepository;
import edu.cfd.e_learningPlatform.service.AssignmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    @Override
    public AssignmentDto findAssignmentInLecture(Long id) {
        return assignmentMapper.toAssignmentDto(assignmentRepository.findByLecture_Id(id));
    }
}