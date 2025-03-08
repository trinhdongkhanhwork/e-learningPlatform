package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.request.CartRequest;
import edu.cfd.e_learningPlatform.dto.response.CartResponse;
import edu.cfd.e_learningPlatform.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "title", source = "course.title")
    @Mapping(target = "price", source = "course.price")
    @Mapping(target = "coverImage", source = "course.coverImage")
    @Mapping(target = "level", source = "course.level")
    @Mapping(target = "courseId", source = "course.id")
    CartResponse cartToCartResponse(Cart cart);

    Cart cartRequestToCart(CartRequest cartRequest);
}
