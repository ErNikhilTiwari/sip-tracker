package com.cg.siptracker.utility;

import com.cg.siptracker.dto.SipSummaryDto;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

@Component
public class CsvReportGenerator {

    public ByteArrayInputStream generate(List<SipSummaryDto> summaryList) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out))) {
            writer.writeNext(new String[]{"Fund Name", "Invested", "Current Value", "XIRR (%)", "CAGR (%)"});

            for (SipSummaryDto dto : summaryList) {
                writer.writeNext(new String[]{
                        dto.getFundName(),
                        String.valueOf(dto.getInvestedAmount()),
                        String.valueOf(dto.getCurrentValue()),
                        String.format("%.2f", dto.getXirr()),
                        String.format("%.2f", dto.getCagr())
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV", e);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}

