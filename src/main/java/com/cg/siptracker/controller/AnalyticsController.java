package com.cg.siptracker.controller;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.SIPRepository;
import com.cg.siptracker.service.IAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private SIPRepository sipRepository;

    @Autowired
    private IAnalyticsService IAnalyticsService;

    // Get SIP performance summary by SIP ID
    @GetMapping("/sip/{id}/summary")
    public ResponseEntity<SipSummaryDto> getSipSummary(@PathVariable Long id) {
        SIP sip = sipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SIP not found with id: " + id));

        SipSummaryDto summary = IAnalyticsService.analyzeSIP(sip);
        return ResponseEntity.ok(summary);
    }
}
