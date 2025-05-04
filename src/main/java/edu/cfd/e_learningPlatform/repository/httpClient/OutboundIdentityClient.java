package edu.cfd.e_learningPlatform.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import edu.cfd.e_learningPlatform.dto.request.ExchangeTokenRequest;
import edu.cfd.e_learningPlatform.dto.response.ExchangeTokenResponse;
import feign.QueryMap;

@FeignClient(name = "outbound-identity-client", url = "https://oauth2.googleapis.com")
public interface OutboundIdentityClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
