package edu.cfd.e_learningPlatform.service;

import edu.cfd.e_learningPlatform.dto.request.UserCreationRequest;
import edu.cfd.e_learningPlatform.dto.response.UserResponse;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.enums.Gender;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void initData() {
        request = UserCreationRequest.builder()
                .username("adminkhanhtd")
                .password(passwordEncoder.encode("adminkhanhtd"))
                .email("khanhtdps36301@fpt.edu.vn")
                .fullname("khanhtd36301")
                .birthday(new Date())
                .gender(Gender.MALE)
                .phone("123456789")
                .avatarUrl("http://example.com/avatar.jpg")
                .build();

        userResponse = UserResponse.builder()
                .id("123abc")
                .username("adminkhanhtd")
                .email("khanhtdps36301@fpt.edu.vn")
                .fullname("khanhtd36301")
                .birthday(new Date())
                .gender(Gender.MALE)
                .phone("123456789")
                .avatarUrl("http://example.com/avatar.jpg")
                .build();

        user = User.builder()
                .id("123abc")
                .username("adminkhanhtd")
                .email("khanhtdps36301@fpt.edu.vn")
                .fullname("khanhtd36301")
                .birthday(new Date())
                .gender(Gender.MALE)
                .phone("123456789")
                .avatarUrl("http://example.com/avatar.jpg")
                .build();
    }

    @Test
    void createUser_vailRequest_success() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString()))
                .thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any()))
                .thenReturn(user);

        //WHEN
        var response = userService.createUser(request);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("123abc");
        Assertions.assertThat(response.getUsername()).isEqualTo("adminkhanhtd");

    }

    @Test
    void createUser_usernameExited_success() {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString()))
                .thenReturn(true);

        //WHEN
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(request));

        //THEN
        Assertions.assertThat(exception.getErrorCode().getCode())
                .isEqualTo(1002);
    }
}
