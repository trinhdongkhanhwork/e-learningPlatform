package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.response.LectueUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import edu.cfd.e_learningPlatform.dto.LectureDto;
import edu.cfd.e_learningPlatform.entity.Lecture;

@Mapper(componentModel = "spring")
public interface LectureMapper {
    public static final LectureMapper INSTANCE = Mappers.getMapper(LectureMapper.class);

    LectueUserResponse lectureToLectueUserResponse(Lecture lecture);

    LectureDto lectureToLectureDto(Lecture lecture);

    Lecture lectureDtoToLecture(LectureDto lectureDto);
}
