package edu.cfd.e_learningPlatform.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.cfd.e_learningPlatform.dto.request.WishlistRequest;
import edu.cfd.e_learningPlatform.dto.response.WishlistDtoResponse;
import edu.cfd.e_learningPlatform.service.WishlistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/wishlist")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WishlistController {

    WishlistService wishlistService;

    @PostMapping("/addWishlist")
    public ResponseEntity<WishlistDtoResponse> addWishlist(@RequestBody WishlistRequest wishlistDto) {
        WishlistDtoResponse savedWishlist = wishlistService.addWishlist(wishlistDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWishlist);
    }

    @PutMapping("/updateWishlist/{id}")
    public ResponseEntity<WishlistDtoResponse> updateWS(
            @PathVariable Long id, @RequestBody WishlistRequest wishlistDto) {
        WishlistDtoResponse updatedWishlist = wishlistService.updateWishlist(id, wishlistDto);
        return ResponseEntity.ok(updatedWishlist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWS(@PathVariable("id") Long id) {
        try {
            wishlistService.deleteWishlist(id);
            return ResponseEntity.ok("Wishlist deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wishlist not found");
        }
    }

    @GetMapping("/getAllWS/{userId}")
    public ResponseEntity<List<WishlistDtoResponse>> getAllWS(@PathVariable String userId) {
        List<WishlistDtoResponse> wishlist = wishlistService.getAllWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }
}
