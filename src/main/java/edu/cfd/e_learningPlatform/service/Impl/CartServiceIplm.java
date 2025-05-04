package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.CartRequest;
import edu.cfd.e_learningPlatform.dto.response.CartResponse;
import edu.cfd.e_learningPlatform.entity.Cart;
import edu.cfd.e_learningPlatform.entity.Course;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.mapstruct.CartMapper;
import edu.cfd.e_learningPlatform.repository.CartRpository;
import edu.cfd.e_learningPlatform.repository.CourseRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class CartServiceIplm implements CartService {

    CartRpository cartRpository;
    CartMapper cartMapper = CartMapper.INSTANCE;
    UserRepository userRepository;
    CourseRepository courseRepository;

    @Override
    public CartResponse addCart(CartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(()->  new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(cartRequest.getCourseId())
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Cart cart = cartMapper.cartRequestToCart(cartRequest);
        cart.setUser(user);
        cart.setCourse(course);
        Cart saveCart = cartRpository.save(cart);
        return cartMapper.cartToCartResponse(saveCart);
    }

    @Override
    public List<CartResponse> getAllCart(String userId) {
        List<Cart> cart = cartRpository.findByUserId(userId);
        return cart.stream().map(cartMapper::cartToCartResponse).toList();
    }

    @Override
    public CartResponse updateCart(Long id, CartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        Course course = courseRepository.findById(cartRequest.getCourseId())
                .orElseThrow(()-> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Cart cart = cartRpository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CART_NOT_FOUND));
        cart.setUser(user);
        cart.setCourse(course);

        Cart updateCart = cartRpository.save(cart);
        return cartMapper.cartToCartResponse(updateCart);
    }

    @Override
    public CartResponse getCartById(Long id) {
        Cart cart = cartRpository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CART_NOT_FOUND));
        return cartMapper.cartToCartResponse(cart);
    }

    @Override
    public void deleteCart(Long id) {
        if(cartRpository.existsById(id)){
            cartRpository.deleteById(id);
        }else{
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }
    }
}
