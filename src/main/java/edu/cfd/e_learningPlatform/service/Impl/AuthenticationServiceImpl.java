package edu.cfd.e_learningPlatform.service.Impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import edu.cfd.e_learningPlatform.dto.request.AuthenticationRequest;
import edu.cfd.e_learningPlatform.dto.request.ExchangeTokenRequest;
import edu.cfd.e_learningPlatform.dto.request.IntrospectRequest;
import edu.cfd.e_learningPlatform.dto.response.AuthenticationResponse;
import edu.cfd.e_learningPlatform.dto.response.IntrospectResponse;
import edu.cfd.e_learningPlatform.entity.Role;
import edu.cfd.e_learningPlatform.entity.User;
import edu.cfd.e_learningPlatform.enums.Gender;
import edu.cfd.e_learningPlatform.exception.AppException;
import edu.cfd.e_learningPlatform.exception.ErrorCode;
import edu.cfd.e_learningPlatform.repository.RoleRepository;
import edu.cfd.e_learningPlatform.repository.UserRepository;
import edu.cfd.e_learningPlatform.repository.httpClient.OutboundIdentityClient;
import edu.cfd.e_learningPlatform.repository.httpClient.OutboundUserClient;
import edu.cfd.e_learningPlatform.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${outbound.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    @Override
    public AuthenticationResponse outboundAuthenticate(String code) {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());

        log.info("TOKEN RESPONSE {}", response);

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        log.info("USER INFO {}", userInfo);

        LocalDateTime now = LocalDateTime.now();
        Role defaultRole =
                roleRepository.findByRoleName("STUDENT").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        var user = userRepository
                .findByEmail(userInfo.getEmail())
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(userInfo.getId())
                        .gender(Gender.PRIVATE)
                        .password("")
                        .email(userInfo.getEmail())
                        .fullname(userInfo.getName())
                        .avatarUrl(userInfo.getPicture())
                        .isActive(false)
                        .roles(Set.of(defaultRole))
                        .build()));
        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("cfd.edu")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (user.getRoles() != null) {
            stringJoiner.add(user.getRoles().stream()
                    .map(Role::getRoleName)
                    .reduce((a, b) -> a + " " + b)
                    .orElse(""));
        }

        return stringJoiner.toString();
    }
}
