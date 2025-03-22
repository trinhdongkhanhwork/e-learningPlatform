package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private UserResponse user;
    private Long friendId;
    private String message;
    private String urlFile;
    private String urlImage;
    private boolean recall;
    private LocalDateTime createdAt;
}
