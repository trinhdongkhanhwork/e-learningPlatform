package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.AssignmentDto;
import edu.cfd.e_learningPlatform.entity.Assignment;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    public static final AssignmentMapper INSTANCE = Mappers.getMapper(AssignmentMapper.class);

    AssignmentDto toAssignmentDto(Assignment assignment);
    Assignment toAssignment(AssignmentDto assignmentDto);
}