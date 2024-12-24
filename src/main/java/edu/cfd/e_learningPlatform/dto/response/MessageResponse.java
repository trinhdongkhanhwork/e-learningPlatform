package edu.cfd.e_learningPlatform.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private String message;
    private String idUserFrom;
    private String nameUserFrom;
    private String avatarUserFrom;
    private Long idAssembly;
    private String urlImage;
    private String urlFile;
    private LocalDateTime createdAt;
}
