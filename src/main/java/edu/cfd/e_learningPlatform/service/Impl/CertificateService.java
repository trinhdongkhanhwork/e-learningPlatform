package edu.cfd.e_learningPlatform.service.Impl;

import edu.cfd.e_learningPlatform.dto.response.CertificateResponse;
import edu.cfd.e_learningPlatform.entity.*;
import edu.cfd.e_learningPlatform.repository.*;
import edu.cfd.e_learningPlatform.service.CourseService;
import edu.cfd.e_learningPlatform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserService userService;
    private final CourseService courseService;


    public List<CertificateResponse> getCertificatesForCurrentUser() {
        User user = userService.getCurrentUser();
        List<Certificate> certificates = certificateRepository.findByUser(user);

        return certificates.stream()
                .map(cert -> new CertificateResponse(
                        cert.getId(),
                        courseService.getCourseById(cert.getCourse().getId()),
                        cert.getIssuedAt()
                ))
                .collect(Collectors.toList());
    }
}
