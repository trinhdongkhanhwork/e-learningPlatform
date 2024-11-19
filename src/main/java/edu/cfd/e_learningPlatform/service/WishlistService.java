package edu.cfd.e_learningPlatform.service;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.request.WishlistRequest;
import edu.cfd.e_learningPlatform.dto.response.WishlistDtoResponse;

public interface WishlistService {

    WishlistDtoResponse addWishlist(WishlistRequest wishlistDto);

    List<WishlistDtoResponse> getAllWishlist(String userId);

    WishlistDtoResponse updateWishlist(Long id, WishlistRequest wishlistDto);

    WishlistDtoResponse getWishlistById(Long id);

    void deleteWishlist(Long id);
}
