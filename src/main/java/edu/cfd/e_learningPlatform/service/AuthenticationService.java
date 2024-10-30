package edu.cfd.e_learningPlatform.service;

import com.nimbusds.jose.JOSEException;
import edu.cfd.e_learningPlatform.dto.request.AuthenticationRequest;
import edu.cfd.e_learningPlatform.dto.request.IntrospectRequest;
import edu.cfd.e_learningPlatform.dto.response.AuthenticationResponse;
import edu.cfd.e_learningPlatform.dto.response.IntrospectResponse;
import edu.cfd.e_learningPlatform.entity.User;

import java.text.ParseException;

public interface AuthenticationService {
    String buildScope(User user);

    String generateToken(User user);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException;
}
