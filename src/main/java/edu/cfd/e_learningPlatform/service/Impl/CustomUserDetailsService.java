package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.entity.CustomUserDetails;
import edu.cfd.e_learningPlatform.entity.Permission;
import edu.cfd.e_learningPlatform.entity.Role;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = getAuthorities(user);

        return new CustomUserDetails(
                authorities,
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFullname(),
                user.getEmail(),
                user.isActive()
        );
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Permission từ Role
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }

        // Permission riêng của User (nếu có)
        for (Permission p : user.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(p.getName()));
        }

        System.out.println("Authorities: " + authorities);
        return authorities;
    }
}

