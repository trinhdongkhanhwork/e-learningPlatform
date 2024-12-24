package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.AssemblyRequest;
import edu.cfd.e_learningPlatform.dto.request.AssemblyUserRequest;
import edu.cfd.e_learningPlatform.dto.response.AssemblyResponse;
import edu.cfd.e_learningPlatform.dto.response.AssemblyUserResponse;

import java.util.List;

public interface AssemblyService {
    List<AssemblyResponse> getAssemblyUserJoin(String idUser);
    AssemblyUserResponse joinAssembly(AssemblyUserRequest assemblyUserRequest);
    String outAssembly(AssemblyUserRequest assemblyUserRequest);
    AssemblyResponse addAssembly(AssemblyRequest assemblyRequest);
    AssemblyResponse updateAssembly(AssemblyRequest assemblyRequest);
    String deleteAssembly(Long idAssembly);
}
