package edu.cfd.e_learningPlatform.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import edu.cfd.e_learningPlatform.dto.request.CourseCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.CourseResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.repository.UserRepository;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class})
public abstract class CourseMapper {
    @Autowired
    private UserRepository userRepository;

    public static final CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "instructor", target = "instructor", qualifiedByName = "stringToUser")
    @Mapping(source = "categoryId", target = "category.id")
    public abstract Course toCourse(CourseCreationRequest courseDto);

    @Mapping(source = "instructor", target = "instructor", qualifiedByName = "userToUserResponse")
    @Mapping(source = "category.id", target = "categoryId")
    public abstract CourseResponse toCourseResponse(Course course);

    @Named("stringToUser")
    User stringToUser(String instructor) {
        return userRepository.findById(instructor).orElseThrow(() -> new RuntimeException("Instructor not found"));
    }

    @Named("userToUserResponse")
    UserResponse userToUserResponse(User user) {
        return UserMapper.INSTANCE.toUserResponse(user);
    }

    @Mapping(source = "instructor", target = "instructor", qualifiedByName = "userToUserResponse")
    @Mapping(source = "category.id", target = "categoryId")
    public abstract List<CourseResponse> toCourseResponses(List<Course> courses);
}
