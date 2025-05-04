package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.request.RolePermissionCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.RolePermissionResponse;
import edu.cfd.e_learningPlatform.entity.Permission;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.mapstruct.PermissionMapper;
import edu.cfd.e_learningPlatform.repository.PermissionRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.PermissionService;
import edu.cfd.e_learningPlatform.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final UserRepository userRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper, UserService userService, UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<String> getAllPermissionsOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<RolePermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .filter(permission -> !permission.getName().equals("ADMIN_SYSTEM"))
                .map(permissionMapper::toRolePermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RolePermissionResponse createPermission(RolePermissionCreationRequest request) {
        if (permissionRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Permission with name " + request.getName() + " already exists");
        }
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toRolePermissionResponse(permission);
    }

    @Override
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new IllegalArgumentException("Permission with ID " + id + " does not exist");
        }
        permissionRepository.deleteById(id);
    }
}
