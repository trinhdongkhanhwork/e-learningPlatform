package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.RolePermissionCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.RolePermissionResponse;

import java.util.List;

public interface PermissionService {

    List<String> getAllPermissionsOfUser(String userId);

    List<RolePermissionResponse> getAllPermissions();

    RolePermissionResponse createPermission(RolePermissionCreationRequest request);

    void deletePermission(Long id);
}
