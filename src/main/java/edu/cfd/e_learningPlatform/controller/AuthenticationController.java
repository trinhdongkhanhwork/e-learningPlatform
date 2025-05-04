package edu.cfd.e_learningPlatform.controller;

import com.nimbusds.jose.JOSEException;
import edu.cfd.e_learningPlatform.dto.request.AuthenticationRequest;
import edu.cfd.e_learningPlatform.dto.request.EmailRequest;
import edu.cfd.e_learningPlatform.dto.request.IntrospectRequest;
import edu.cfd.e_learningPlatform.dto.request.VerifyOtpRequest;
import edu.cfd.e_learningPlatform.dto.response.*;
import edu.cfd.e_learningPlatform.service.AuthenticationService;
import edu.cfd.e_learningPlatform.service.EmailService;
import edu.cfd.e_learningPlatform.service.Impl.CustomUserDetailsService;
import edu.cfd.e_learningPlatform.service.Impl.JwtService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin("*")
public class AuthenticationController {
    AuthenticationService authenticationService;
    EmailService emailService;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authManager;
    JwtService jwtService;
    CustomUserDetailsService userDetailsService;

    @PostMapping("/outbound/authentication")
    ApiResponse<AuthenticationResponse> outboundAuthenticate(@RequestParam("code") String code) {
        var result = authenticationService.outboundAuthenticate(code);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/token")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);
        // Return the JWT token in the response
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(token);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        try {
            String token = request.getToken();
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean valid = jwtService.isTokenValid(token, userDetails);
            IntrospectResponse introspectResponse = new IntrospectResponse();
            introspectResponse.setValid(valid);
            return ResponseEntity.ok(introspectResponse);
        } catch (Exception e) {
            throw new RuntimeException("Token introspection failed", e);
        }
    }

    @PostMapping("/forgot-password")
    ApiResponse<EmailResponse> forgotPassword(@RequestBody EmailRequest request) throws MessagingException {
        String otp = emailService.generateOTP(request.getEmail());
        emailService.sendOTPEmail(request.getEmail(), otp);
        return ApiResponse.<EmailResponse>builder()
                .result(EmailResponse.builder()
                        .hashedOtp(passwordEncoder.encode(otp))
                        .creationTime(LocalDateTime.now())
                        .build())
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<VerifyOtpResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {
        LocalDateTime expirationTime = LocalDateTime.now();
        boolean isOtpValid = emailService.verifyOTP(
                request.getOtp(), request.getHashedOtp(), request.getCreationTime(), expirationTime);
        return ApiResponse.<VerifyOtpResponse>builder()
                .result(VerifyOtpResponse.builder().valid(isOtpValid).build())
                .build();
    }

    @PostMapping("/send-email-creation-user")
    ApiResponse<EmailResponse> creationUser(@RequestBody EmailRequest request) throws MessagingException {
        String otp = emailService.generateOTP(request.getEmail());
        emailService.sendOTPEmailForCreationUser(request.getEmail(), request.getUsername(), otp);
        return ApiResponse.<EmailResponse>builder()
                .result(EmailResponse.builder()
                        .hashedOtp(passwordEncoder.encode(otp))
                        .creationTime(LocalDateTime.now())
                        .build())
                .build();
    }
}
