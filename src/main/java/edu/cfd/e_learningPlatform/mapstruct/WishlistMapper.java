package edu.cfd.e_learningPlatform.mapstruct;


import edu.cfd.e_learningPlatform.dto.request.WishlistRequest;
import edu.cfd.e_learningPlatform.dto.response.WishlistDtoResponse;
import edu.cfd.e_learningPlatform.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistMapper INSTANCE = Mappers.getMapper(WishlistMapper.class);

    @Mapping(target = "title", source = "course.title")
    @Mapping(target = "price", source = "course.price")
    @Mapping(target = "coverImage", source = "course.coverImage")
    @Mapping(target = "level", source = "course.level")
    @Mapping(target = "courseId", source = "course.id")
    WishlistDtoResponse wishlistToWishlistDtoResponse(Wishlist wishlist);

    Wishlist wishlistDtoToWishlist(WishlistRequest wishlistDto);
}
