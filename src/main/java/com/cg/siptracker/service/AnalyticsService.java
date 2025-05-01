package com.cg.siptracker.service;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.model.SIP;

import java.time.LocalDate;
import java.util.Map;

public interface AnalyticsService {

    ResponseDTO getSipSummary(Long sipId, String email);
    Map<LocalDate, Double> generateCashFlows(SIP sip);
    double calculateXIRR(Map<LocalDate, Double> cashFlows);
    double calculateCAGR(Map<LocalDate, Double> cashFlows);
}
