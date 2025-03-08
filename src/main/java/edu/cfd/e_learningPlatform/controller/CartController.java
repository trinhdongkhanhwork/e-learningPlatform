package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.CartRequest;
import edu.cfd.e_learningPlatform.dto.response.CartResponse;
import edu.cfd.e_learningPlatform.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartController {

    CartService cartService;

    @PostMapping("/addCart")
    public ResponseEntity<CartResponse> addCart(@RequestBody CartRequest cartRequest){
        CartResponse saveResponse = cartService.addCart(cartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long id, @RequestBody CartRequest cartRequest){
        CartResponse updateResponse = cartService.updateCart(id, cartRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id){
        cartService.deleteCart(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/getAllCart/{userId}")
    public ResponseEntity<List<CartResponse>> getAllCart(@PathVariable String userId){
        List<CartResponse> cart = cartService.getAllCart(userId);
        return ResponseEntity.ok(cart);
    }
}
