package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.AssemblyRequest;
import edu.cfd.e_learningPlatform.dto.request.AssemblyUserRequest;
import edu.cfd.e_learningPlatform.dto.response.AssemblyResponse;
import edu.cfd.e_learningPlatform.dto.response.AssemblyUserResponse;
import edu.cfd.e_learningPlatform.entity.Assembly;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.mapstruct.AssemblyMapper;
import edu.cfd.e_learningPlatform.repository.AssemblyRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.AssemblyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssemblyServiceImpl implements AssemblyService {

    AssemblyRepository assemblyRepository;
    UserRepository userRepository;
    AssemblyMapper assemblyMapper;

    @Override
    public List<AssemblyResponse> getAssemblyUserJoin(String idUser) {
        return assemblyMapper.AssemblyToAssemblyResponses(assemblyRepository.getAssemblyUserJoin(idUser));
    }

    @Override
    public AssemblyUserResponse joinAssembly(AssemblyUserRequest assemblyUserRequest) {
        if (assemblyRepository.existsUserJoinGroup(assemblyUserRequest.getIdAssembly(), assemblyUserRequest.getIdUser()) == 0) {
            Assembly assembly = assemblyRepository.findById(assemblyUserRequest.getIdAssembly()).orElseThrow(() -> new RuntimeException("Assembly not found"));
            User user = userRepository.findById(assemblyUserRequest.getIdUser()).orElseThrow(() -> new RuntimeException("User not found"));
            assembly.getUserAssemblys().add(user);
            assemblyRepository.save(assembly);
            return new AssemblyUserResponse(user.getUsername(), assembly.getNameAssembly());
        } else {
            return new AssemblyUserResponse(null, null);
        }
    }

    @Override
    public String outAssembly(AssemblyUserRequest assemblyUserRequest) {
        if (assemblyRepository.existsUserJoinGroup(assemblyUserRequest.getIdAssembly(), assemblyUserRequest.getIdUser()) == 1) {
            Assembly assembly = assemblyRepository.findById(assemblyUserRequest.getIdAssembly()).orElseThrow(() -> new RuntimeException("Assembly not found"));
            User user = userRepository.findById(assemblyUserRequest.getIdUser()).orElseThrow(() -> new RuntimeException("User not found"));
            assembly.getUserAssemblys().remove(user);
            assemblyRepository.save(assembly);
            return "Out successfully";
        } else {
            return "User not in assembly";
        }
    }

    @Override
    public AssemblyResponse addAssembly(AssemblyRequest assemblyRequest) {
        User user = userRepository.findById(assemblyRequest.getAdminId()).orElseThrow(() -> new RuntimeException("User not found"));
        Assembly assembly = assemblyMapper.AssemblyRequestToAssembly(assemblyRequest);
        assembly.setId(null);
        assembly.setAdmin(user);
        return assemblyMapper.AssemblyToAssemblyResponse(assemblyRepository.save(assembly));
    }

    @Override
    public AssemblyResponse updateAssembly(AssemblyRequest assemblyRequest) {
        User user = userRepository.findById(assemblyRequest.getAdminId()).orElseThrow(() -> new RuntimeException("User not found"));
        Assembly assembly = assemblyRepository.findById(assemblyRequest.getId()).orElseThrow(() -> new RuntimeException("Assembly not found"));
        assembly.setId(assemblyRequest.getId());
        assembly.setAdmin(user);
        assembly.setImageAssembly(assemblyRequest.getImageAssembly());
        assembly.setNameAssembly(assemblyRequest.getNameAssembly());
        assembly.setActive(assemblyRequest.isActive());
        return assemblyMapper.AssemblyToAssemblyResponse(assemblyRepository.save(assembly));
    }

    @Override
    public String deleteAssembly(Long idAssembly) {
        Assembly assembly = assemblyRepository.findById(idAssembly).orElseThrow(() -> new RuntimeException("Assembly not found"));
        assembly.setActive(false);
        assemblyRepository.save(assembly);
        return "Assembly deleted";
    }
}