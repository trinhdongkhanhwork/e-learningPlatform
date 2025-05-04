package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.RatingRequest;
import edu.cfd.e_learningPlatform.dto.response.RatingResponse;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.Rating;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.mapstruct.RatingMapper;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.PaymentRepository;
import edu.cfd.e_learningPlatform.repository.RatingRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;
    private final RatingMapper ratingMapper;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(UserRepository userRepository, CourseRepository courseRepository, PaymentRepository paymentRepository, RatingMapper ratingMapper, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.paymentRepository = paymentRepository;
        this.ratingMapper = ratingMapper;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public RatingResponse createRating(RatingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if (!paymentRepository.existsByUserIdAndCourseIdAndEnrollment(request.getUserId(), request.getCourseId(), true)) {
            throw new AppException(ErrorCode.ENROLLMENT_FALSE);
        }
        Rating rating = ratingMapper.toEntity(request);
        rating.setUser(user);
        rating.setCourse(course);
        Rating savedRating = ratingRepository.save(rating);
        return ratingMapper.toResponse(savedRating);
    }

    @Override
    public RatingResponse updateRating(RatingRequest request) {
        if (request.getRatingId() == null) {
            throw new AppException(ErrorCode.RATING_NOT_FOUND);
        }
        Rating rating = ratingRepository.findByIdAndUserId(request.getRatingId(), request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.RATING_USER_NOT_FOUND));
        if (!paymentRepository.existsByUserIdAndCourseIdAndEnrollment(request.getUserId(), rating.getCourse().getId(), true)) {
            throw new AppException(ErrorCode.ENROLLMENT_FALSE);
        }
        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        Rating updatedRating = ratingRepository.save(rating);
        return ratingMapper.toResponse(updatedRating);
    }

    @Override
    public void deleteRating(RatingRequest request) {
        if (request.getRatingId() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        Rating rating = ratingRepository.findByIdAndUserId(request.getRatingId(), request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.RATING_USER_NOT_FOUND));
        if (!paymentRepository.existsByUserIdAndCourseIdAndEnrollment(request.getUserId(), rating.getCourse().getId(), true)) {
            throw new AppException(ErrorCode.ENROLLMENT_FALSE);
        }
        ratingRepository.delete(rating);
    }

    @Override
    public List<RatingResponse> getRatingsByCourseId(Long courseId) {
        List<Rating> ratings = ratingRepository.findByCourseId(courseId);
        return ratings.stream().map(ratingMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Double getAverageRatingByCourseId(Long courseId) {
        return ratingRepository.findAverageRatingByCourseId(courseId);
    }
}
