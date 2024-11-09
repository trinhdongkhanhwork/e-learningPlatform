package edu.cfd.e_learningPlatform.mapstruct;


import edu.cfd.e_learningPlatform.dto.LectureDto;
import edu.cfd.e_learningPlatform.entity.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LectureMapper {
    LectureMapper INSTANCE = Mappers.getMapper(LectureMapper.class);

    LectureDto lectureToLectureDto(Lecture lecture);

    Lecture lectureDtoToLecture(LectureDto lectureDto);

}
