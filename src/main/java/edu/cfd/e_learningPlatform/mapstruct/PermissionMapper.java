package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.request.RolePermissionCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.RolePermissionResponse;
import edu.cfd.e_learningPlatform.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    Permission toPermission(RolePermissionCreationRequest request);

    RolePermissionResponse toRolePermissionResponse(Permission permission);
}
