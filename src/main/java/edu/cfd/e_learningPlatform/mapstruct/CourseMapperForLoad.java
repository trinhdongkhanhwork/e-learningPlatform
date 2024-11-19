package edu.cfd.e_learningPlatform.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import edu.cfd.e_learningPlatform.dto.CourseDto;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.Lecture;
import edu.cfd.e_learningPlatform.entity.Section;

@Mapper(componentModel = "spring")
public interface CourseMapperForLoad {
    @Mapping(target = "sections", source = "sections")
    CourseDto toDto(Course course);

    @Mapping(target = "sections", ignore = true)
    Course toEntity(CourseDto courseDto);

    @Mapping(target = "lectures", source = "lectures")
    CourseDto.SectionDto toSectionDto(Section section);

    @Mapping(target = "lectures", ignore = true)
    Section toSectionEntity(CourseDto.SectionDto sectionDto);

    @Mapping(target = "content", expression = "java(mapLectureContent(lecture))")
    CourseDto.LectureDto toLectureDto(Lecture lecture);

    @Mapping(target = "section", ignore = true)
    Lecture toLectureEntity(CourseDto.LectureDto lectureDto);

    default String mapLectureContent(Lecture lecture) {
        if ("video".equals(lecture.getType())) {
            return lecture.getVideos().isEmpty()
                    ? null
                    : lecture.getVideos().get(0).getVideoUrl();
        } else if ("quiz".equals(lecture.getType())) {
            return lecture.getQuiz() == null
                    ? null
                    : String.valueOf(lecture.getQuiz().getId());
        } else {
            return null;
        }
    }
}
