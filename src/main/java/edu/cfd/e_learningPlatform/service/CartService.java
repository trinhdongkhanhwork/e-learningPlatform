package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.CartRequest;
import edu.cfd.e_learningPlatform.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    CartResponse addCart(CartRequest cartRequest);
    List<CartResponse> getAllCart(String userId);
    CartResponse updateCart(Long id, CartRequest cartRequest);
    CartResponse getCartById(Long id);
    void deleteCart(Long id);
}
