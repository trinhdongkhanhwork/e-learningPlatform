package edu.cfd.e_learningPlatform.service;

import java.util.List;

import edu.cfd.e_learningPlatform.dto.request.ProfileUpdateRequest;
import edu.cfd.e_learningPlatform.dto.request.UpdatePassWordRequest;
import edu.cfd.e_learningPlatform.dto.request.UserCreationRequest;
import edu.cfd.e_learningPlatform.dto.request.UserUpdateRequest;
import edu.cfd.e_learningPlatform.dto.response.ProfileUpdateResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.User;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse getMyInfo();

    List<UserResponse> getUsers();

    List<UserResponse> getUsersUpdateTeacher();

    UserResponse getUser(String id);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void updateTeacher(String userId);

    void updateRoles(String userId);

    void deleteUser(String userId);

    void updatePassWord(String email, UpdatePassWordRequest request, boolean vail);

    User getCurrentUser();

    ProfileUpdateResponse updateProfile(String userId, ProfileUpdateRequest request);

    long getUserCountByRoleId();
}