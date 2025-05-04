package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.LectueUserResponse;
import edu.cfd.e_learningPlatform.service.LectureService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
