package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.response.EarningsSummaryResponse;
import edu.cfd.e_learningPlatform.service.TransactionsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class TransactionsController {

    TransactionsService transactionsService;

    @GetMapping("/user/{id}/earnings-summary")
    public ResponseEntity<EarningsSummaryResponse> getEarningsSummary(@PathVariable String id){
        EarningsSummaryResponse summary = transactionsService.getEarningsSummary(id);
        return ResponseEntity.ok(summary);
    }
}
