package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.*;
import edu.cfd.e_learningPlatform.dto.response.StaffResponse;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.User;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    Boolean isRegisterInstructor();

//    public UserResponse deleteInstructor(String userId);

    public UserResponse registerInstructor();

//    public UserResponse accessInstructor(String userId);

    public UserResponse notAccessInstructor(String userId);

    UserResponse getMyInfo();

    List<UserResponse> getUsers();

    List<UserResponse> getUsersUpdateTeacher();

    UserResponse getUser(String id);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void updateTeacher(String userId);

    void updateRoles(String userId);

    void deleteUser(String userId);

    void updatePassWord(String email, UpdatePassWordRequest request, boolean vail);

    UserResponse updateProfile(String userId, ProfileUpdateRequest request);

    User getCurrentUser();

//    long getUserCountByRoleId();

    UserResponse createAccountStaff(StaffCreationRequest request);

    List<StaffResponse> getStaffs();
}
