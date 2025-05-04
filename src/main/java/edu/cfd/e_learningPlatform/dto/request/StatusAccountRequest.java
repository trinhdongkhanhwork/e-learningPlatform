package edu.cfd.e_learningPlatform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusAccountRequest {
    private String username;
    private String email;
    private boolean accountStatus;
    private String text;
}
