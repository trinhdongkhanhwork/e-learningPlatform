package edu.cfd.e_learningPlatform.config;

import edu.cfd.e_learningPlatform.entity.Role;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.enums.Gender;
import edu.cfd.e_learningPlatform.repository.RoleRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (roleRepository.count() == 0){
                List<Role> roles = new ArrayList<>();
                Role role1 = new Role();
                role1.setRoleName("INSTRUCTOR");

                Role role2 = new Role();
                role2.setRoleName("STUDENT");

                roles.add(role1);
                roles.add(role2);

                roleRepository.saveAll(roles);
            }
            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseGet(() -> {
                        Role role = Role.builder()
                                .roleName("ADMIN")
                                .createdDate(LocalDateTime.now())
                                .updatedDate(LocalDateTime.now())
                                .build();
                        return roleRepository.save(role);
                    });

            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("khanhtdps36301@fpt.edu.vn")
                        .fullname("khanhtd36301")
                        .birthday(new Date())
                        .gender(Gender.MALE)
                        .phone("123456789")
                        .roleEntity(adminRole)
                        .avatarUrl("http://example.com/avatar.jpg")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .isActive(true)
                        .build();
                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin, please change it.");
            }
        };
    }
}
