package com.cg.siptracker.controller;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.SIPRepository;
import com.cg.siptracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private SipRepository sipRepository;

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/sip/{id}/summary")
    public ResponseEntity<SipSummaryDto> getSipSummary(@PathVariable Long id) {
        SIP sip = sipRepository.findById(id).orElseThrow(() ->
                new RuntimeException("SIP not found with id " + id));
        SipSummaryDto summary = analyticsService.analyzeSIP(sip);
        return ResponseEntity.ok(summary);
    }
}
