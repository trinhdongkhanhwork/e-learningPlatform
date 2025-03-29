package edu.cfd.e_learningPlatform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private Long id;
    private String userId;
    private String friendId;
    private String message;
    private String urlFile;
    private String urlImage;
}
