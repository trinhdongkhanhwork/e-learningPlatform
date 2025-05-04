package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.WishlistRequest;
import edu.cfd.e_learningPlatform.dto.response.WishlistDtoResponse;

import java.util.List;

public interface WishlistService {

    WishlistDtoResponse addWishlist(WishlistRequest wishlistDto);

    List<WishlistDtoResponse> getAllWishlist(String userId);

    WishlistDtoResponse updateWishlist(Long id, WishlistRequest wishlistDto);

    WishlistDtoResponse getWishlistById(Long id);

    void deleteWishlist(Long id);
}
