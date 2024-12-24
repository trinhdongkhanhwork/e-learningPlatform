package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.request.AssemblyRequest;
import edu.cfd.e_learningPlatform.dto.response.AssemblyResponse;
import edu.cfd.e_learningPlatform.entity.Assembly;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssemblyMapper {

    Assembly AssemblyRequestToAssembly(AssemblyRequest assemblyRequest);

    @Mapping(source = "admin.id", target = "adminId")
    AssemblyResponse AssemblyToAssemblyResponse(Assembly assembly);

    @Mapping(source = "admin.id", target = "adminId")
    List<AssemblyResponse> AssemblyToAssemblyResponses(List<Assembly> assemblys);
}