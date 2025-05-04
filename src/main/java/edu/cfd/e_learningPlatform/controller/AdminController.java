package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.RolePermissionCreationRequest;
import edu.cfd.e_learningPlatform.dto.request.StaffCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.RolePermissionResponse;
import edu.cfd.e_learningPlatform.dto.response.StaffResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.service.PermissionService;
import edu.cfd.e_learningPlatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AdminController {
    private final PermissionService permissionService;
    private final UserService userService;

    public AdminController(PermissionService permissionService, UserService userService) {
        this.permissionService = permissionService;
        this.userService = userService;
    }


    @GetMapping("/getAllPermissions")
    public List<RolePermissionResponse> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/getAllPermissionsOfUser")
    public List<String> getAllPermissionsOfUser(String userId) {
        return permissionService.getAllPermissionsOfUser(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN_SYSTEM')")
    @PostMapping("/createPermission")
    public RolePermissionResponse createPermission(@Valid @RequestBody RolePermissionCreationRequest request) {
        return permissionService.createPermission(request);
    }

    @PreAuthorize("hasAuthority('ADMIN_SYSTEM')")
    @PostMapping("/CreatAccountStaff")
    public UserResponse createAccountStaff(@RequestBody StaffCreationRequest request) {
        return userService.createAccountStaff(request);
    }

    @PreAuthorize("hasAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/deletePermission")
    public ResponseEntity<Map<String, String>> deletePermission(Long permissionId) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.ok(Map.of("message", "Permission " + permissionId + " deleted successfully"));
    }


    @GetMapping("/getAllStaffs")
    public List<StaffResponse> getAllStaffs() {
        return userService.getStaffs();
    }
}
