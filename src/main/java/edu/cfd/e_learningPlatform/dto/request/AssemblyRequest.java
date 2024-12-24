package edu.cfd.e_learningPlatform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssemblyRequest {
    private Long id;
    private String nameAssembly;
    private String imageAssembly;
    private String adminId;
    private boolean active;
}
