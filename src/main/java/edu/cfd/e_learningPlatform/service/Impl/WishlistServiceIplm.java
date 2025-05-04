package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.WishlistRequest;
import edu.cfd.e_learningPlatform.dto.response.WishlistDtoResponse;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.entity.Wishlist;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.mapstruct.WishlistMapper;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.repository.WishlistRepository;
import edu.cfd.e_learningPlatform.service.WishlistService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WishlistServiceIplm implements WishlistService {

    WishlistMapper wishlistMapper = WishlistMapper.INSTANCE;
    WishlistRepository wishlistRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;

    @Override
    public WishlistDtoResponse addWishlist(WishlistRequest wishlistDto) {
        User user = userRepository
                .findById(wishlistDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository
                .findById(wishlistDto.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Wishlist wishlist = wishlistMapper.wishlistDtoToWishlist(wishlistDto);
        wishlist.setUser(user);
        wishlist.setCourse(course);
        Wishlist saveWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.wishlistToWishlistDtoResponse(saveWishlist);
    }

    @Override
    public List<WishlistDtoResponse> getAllWishlist(String userId) {
        List<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
        return wishlist.stream()
                .map(wishlistMapper::wishlistToWishlistDtoResponse)
                .toList();
    }

    @Override
    public WishlistDtoResponse updateWishlist(Long id, WishlistRequest wishlistDto) {
        User user = userRepository
                .findById(wishlistDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = courseRepository
                .findById(wishlistDto.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Wishlist wishlist =
                wishlistRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOT_FOUND));
        wishlist.setCourse(course);
        wishlist.setUser(user);

        Wishlist updateWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.wishlistToWishlistDtoResponse(updateWishlist);
    }

    @Override
    public WishlistDtoResponse getWishlistById(Long id) {
        Wishlist wishlist =
                wishlistRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOT_FOUND));
        return wishlistMapper.wishlistToWishlistDtoResponse(wishlist);
    }

    @Override
    public void deleteWishlist(Long id) {
        if (wishlistRepository.existsById(id)) {
            wishlistRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Wishlist not found");
        }
    }
}
