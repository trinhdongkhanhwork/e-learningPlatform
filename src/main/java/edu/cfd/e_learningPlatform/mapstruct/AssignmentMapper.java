package edu.cfd.e_learningPlatform.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import edu.cfd.e_learningPlatform.dto.AssignmentDto;
import edu.cfd.e_learningPlatform.entity.Assignment;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    public static final AssignmentMapper INSTANCE = Mappers.getMapper(AssignmentMapper.class);

    AssignmentDto toAssignmentDto(Assignment assignment);

}
