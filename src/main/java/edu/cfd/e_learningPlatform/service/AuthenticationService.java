package edu.cfd.e_learningPlatform.service;

import com.nimbusds.jose.JOSEException;
import edu.cfd.e_learningPlatform.dto.request.AuthenticationRequest;
import edu.cfd.e_learningPlatform.dto.request.IntrospectRequest;
import edu.cfd.e_learningPlatform.dto.response.AuthenticationResponse;
import edu.cfd.e_learningPlatform.dto.response.IntrospectResponse;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    AuthenticationResponse outboundAuthenticate(String code);
}
