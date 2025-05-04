package edu.cfd.e_learningPlatform.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import edu.cfd.e_learningPlatform.config.AuditorAwareImpl;
import edu.cfd.e_learningPlatform.dto.request.ProfileUpdateRequest;
import edu.cfd.e_learningPlatform.dto.response.ProfileUpdateResponse;
import edu.cfd.e_learningPlatform.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.cfd.e_learningPlatform.dto.request.UpdatePassWordRequest;
import edu.cfd.e_learningPlatform.dto.request.UserCreationRequest;
import edu.cfd.e_learningPlatform.dto.request.UserUpdateRequest;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.Role;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.mapstruct.UserMapper;
import edu.cfd.e_learningPlatform.repository.RoleRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditorAwareImpl auditorAware;
    private final EmailService emailService;


    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        LocalDateTime now = LocalDateTime.now();
        Role defaultRole =
                roleRepository.findByRoleName("STUDENT").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRoleEntity(defaultRole);
        user.setCreatedDate(now);
        user.setActive(false);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse deleteInstructor(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role role = roleRepository.findByRoleName("STUDENT").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoleEntity(role);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse registerInstructor(){
        User user = getCurrentUser();
        user.setActive(true);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse accessInstructor(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role role = roleRepository.findByRoleName("INSTRUCTOR").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoleEntity(role);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse notAccessInstructor(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setActive(false);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void updateRoles(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role defaultRole = roleRepository
                .findByRoleName("INSTRUCTOR")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRoleEntity(defaultRole);
        userRepository.save(user);
    }

    @Override
    public void updateTeacher(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> getUsers() {
        log.info("Get all users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Transactional
    @Override
    public List<UserResponse> getUsersUpdateTeacher() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.isActive() || "INSTRUCTOR".equals(user.getRoleEntity().getRoleName()))
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        log.info("Get user with id {}", id);
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public void updatePassWord(String email, UpdatePassWordRequest request, boolean vail) {

        if (!vail) {
            return;
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request) {
        // Tìm user theo ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Cập nhật các trường nếu có dữ liệu từ request
        if (request.getFullname() != null && !request.getFullname().isEmpty()) {
            user.setFullname(request.getFullname());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        } else {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            user.setPhone(request.getPhone());
        }
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        // Lưu user đã cập nhật
        User updatedUser = userRepository.save(user);

        // Trả về response
        return ProfileUpdateResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .fullname(updatedUser.getFullname())
                .phone(updatedUser.getPhone())
                .roleEntity(updatedUser.getRoleEntity())
                .avatarUrl(updatedUser.getAvatarUrl())
                .build();
    }

    @Override
    public long getUserCountByRoleId() {
        return userRepository.countUsersByRoleId();
    }

    @Override
    public User getCurrentUser() {
        String username = auditorAware.getCurrentAuditor().orElse("Anonymous");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}