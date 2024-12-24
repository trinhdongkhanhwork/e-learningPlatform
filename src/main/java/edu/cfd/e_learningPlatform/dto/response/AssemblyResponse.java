package edu.cfd.e_learningPlatform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyResponse {
    private Long id;
    private String nameAssembly;
    private String imageAssembly;
    private String adminId;
    private boolean active;
}
