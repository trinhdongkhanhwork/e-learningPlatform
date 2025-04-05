package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.request.RatingRequest;
import edu.cfd.e_learningPlatform.dto.response.RatingResponse;
import edu.cfd.e_learningPlatform.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    @Mapping(target = "course.id" , source = "courseId")
    Rating  toEntity(RatingRequest request);
    @Mapping(source = "course.id",target = "courseId")
    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "user.fullname",target = "fullname")
    RatingResponse toResponse (Rating rating);
}
