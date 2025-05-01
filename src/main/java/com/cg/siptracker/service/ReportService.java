package com.cg.siptracker.service;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.SipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private SipRepository sipRepository;

    @Autowired
    private AnalyticsService analyticsService;

    public void generateCsvReport(PrintWriter writer) {
        List<SIP> sips = sipRepository.findAll();

        writer.println("Fund Name,Invested Amount,Current Value,XIRR (%),CAGR (%)");

        for (SIP sip : sips) {
            SipSummaryDto dto = analyticsService.analyzeSIP(sip);
            writer.printf("%s,%.2f,%.2f,%.2f,%.2f%n",
                    dto.getFundName(),
                    dto.getInvestedAmount(),
                    dto.getCurrentValue(),
                    dto.getXirr(),
                    dto.getCagr());
        }
    }
}
