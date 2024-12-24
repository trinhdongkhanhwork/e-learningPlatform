package edu.cfd.e_learningPlatform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyUserRequest {
    private String idUser;
    private Long idAssembly;
}
