package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.CertificateResponse;
import edu.cfd.e_learningPlatform.service.Impl.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;

    @GetMapping
    public ResponseEntity<List<CertificateResponse>> getUserCertificates() {
        List<CertificateResponse> certificates = certificateService.getCertificatesForCurrentUser();
        return ResponseEntity.ok(certificates);
    }
}

