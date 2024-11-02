package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.UpdatePassWordRequest;
import edu.cfd.e_learningPlatform.dto.request.UserCreationRequest;
import edu.cfd.e_learningPlatform.dto.request.UserUpdateRequest;
import edu.cfd.e_learningPlatform.dto.response.ApiResponse;
import edu.cfd.e_learningPlatform.dto.response.UpdatePassWordResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@CrossOrigin("*")
public class UserController {
    UserService userService;
    PasswordEncoder passwordEncoder;

    @PostMapping()
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @PutMapping("/updatePassWord/{email}/{vail}")
    ApiResponse<UpdatePassWordResponse> updatePassWord(@PathVariable String email, @PathVariable boolean vail, @RequestBody UpdatePassWordRequest request) {
        userService.updatePassWord(email, request, vail);
        return ApiResponse.<UpdatePassWordResponse>builder()
                .result(UpdatePassWordResponse.builder()
                        .response(vail)
                        .build())
                .build();
    }
}
