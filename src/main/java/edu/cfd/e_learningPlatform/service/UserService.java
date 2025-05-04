package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.*;
import edu.cfd.e_learningPlatform.dto.response.ProfileUpdateResponse;
import edu.cfd.e_learningPlatform.dto.response.StaffResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.User;

import java.util.List;

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

//    long getUserCountByRoleId();

    UserResponse createAccountStaff(StaffCreationRequest request);

    List<StaffResponse> getStaffs();
}
