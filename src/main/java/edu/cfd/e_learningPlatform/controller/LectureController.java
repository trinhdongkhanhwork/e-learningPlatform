package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.LectueUserResponse;
import edu.cfd.e_learningPlatform.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController()
@RequestMapping("/lectures")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LectureController {
    LectureService lectureService;

    @GetMapping("/{idLecture}")
    public ResponseEntity<LectueUserResponse> getLecture(@PathVariable Long idLecture) {
        return ResponseEntity.ok(lectureService.getLectureUserById(idLecture));
    }

//    @GetMapping
//    ApiResponse<List<UserResponse>> getUsers() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Username: {}", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
//
//        return ApiResponse.<List<UserResponse>>builder()
//                .result(userService.getUsers())
//                .build();
//    }
}
