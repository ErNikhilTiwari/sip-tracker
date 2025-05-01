package com.cg.siptracker.service;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.exception.ResourceNotFoundException;
import com.cg.siptracker.model.NAVRecord;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.NAVRecordRepository;
import com.cg.siptracker.repository.SIPRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private SIPRepository sipRepository;

    @Autowired
    private  NAVRecordRepository navRecordRepository;

    @Override
    public ResponseDTO getSipSummary(Long sipId, String email) {
        SIP sip = sipRepository.findById(sipId)
                .orElseThrow(() -> new ResourceNotFoundException("SIP not found with ID: " + sipId));

        if (!sip.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("You are not authorized to access this SIP");
        }

        Map<LocalDate, Double> cashFlows = generateCashFlows(sip);
        double xirr = calculateXIRR(cashFlows);
        double cagr = calculateCAGR(cashFlows);

        double totalInvested = cashFlows.entrySet().stream()
                .filter(e -> e.getValue() < 0)
                .mapToDouble(Map.Entry::getValue)
                .sum() * -1;

        double currentValue = cashFlows.getOrDefault(LocalDate.now(), 0.0);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("fundName", sip.getFundName());
        result.put("totalInvested", totalInvested);
        result.put("currentValue", currentValue);
        result.put("XIRR", xirr);
        result.put("CAGR", cagr);

        return new ResponseDTO("SIP summary fetched", result);
    }

    @Override
    public Map<LocalDate, Double> generateCashFlows(SIP sip) {
        Map<LocalDate, Double> flows = new TreeMap<>();
        LocalDate startDate = sip.getStartDate();
        LocalDate now = LocalDate.now();
        double amount = sip.getAmount();

        ChronoUnit unit = sip.getFrequency().name().equalsIgnoreCase("MONTHLY") ?
                ChronoUnit.MONTHS : ChronoUnit.WEEKS;

        for (LocalDate date = startDate; !date.isAfter(now); date = date.plus(1, unit)) {
            NAVRecord nav = navRecordRepository.findByFundNameAndNavDate(sip.getFundName(), date)
                    .orElse(null);
            if (nav != null) {
                flows.put(date, -amount);
            }
        }

        // Add latest value as positive cash flow (current value)
        NAVRecord latestNav = navRecordRepository
                .findTopByFundNameOrderByNavDateDesc(sip.getFundName())
                .orElseThrow(() -> new ResourceNotFoundException("No NAV available for " + sip.getFundName()));

        long totalUnits = flows.size();
        double units = (totalUnits * amount) / latestNav.getNavValue();
        flows.put(now, units * latestNav.getNavValue());

        return flows;
    }

    @Override
    public double calculateXIRR(Map<LocalDate, Double> cashFlows) {
        if (cashFlows.size() < 2) return 0;

        List<LocalDate> dates = new ArrayList<>(cashFlows.keySet());
        List<Double> values = new ArrayList<>(cashFlows.values());

        LocalDate start = dates.get(0);

        UnivariateFunction irrFunction = rate -> {
            double npv = 0;
            for (int i = 0; i < dates.size(); i++) {
                long days = ChronoUnit.DAYS.between(start, dates.get(i));
                npv += values.get(i) / Math.pow(1 + rate, days / 365.0);
            }
            return npv;
        };

        NewtonRaphsonSolver solver = new NewtonRaphsonSolver();
        try {
            return solver.solve(1000, (UnivariateDifferentiableFunction) irrFunction, -0.999, 10.0);
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public double calculateCAGR(Map<LocalDate, Double> cashFlows) {
        if (cashFlows.size() < 2) return 0;

        double invested = cashFlows.entrySet().stream()
                .filter(e -> e.getValue() < 0)
                .mapToDouble(Map.Entry::getValue)
                .sum() * -1;

        double endValue = cashFlows.getOrDefault(LocalDate.now(), 0.0);

        LocalDate start = cashFlows.keySet().iterator().next();
        long years = ChronoUnit.DAYS.between(start, LocalDate.now()) / 365;

        if (years == 0 || invested == 0) return 0;

        return Math.pow(endValue / invested, 1.0 / years) - 1;
    }

    @Override
    public SipSummaryDto analyzeSIP(SIP sip) {
        Map<LocalDate, Double> cashFlows = generateCashFlows(sip);
        double xirr = calculateXIRR(cashFlows);
        double cagr = calculateCAGR(cashFlows);

        double totalInvested = cashFlows.entrySet().stream()
                .filter(e -> e.getValue() < 0)
                .mapToDouble(Map.Entry::getValue)
                .sum() * -1;

        double currentValue = cashFlows.getOrDefault(LocalDate.now(), 0.0);

        return new SipSummaryDto(
                sip.getFundName(),
                totalInvested,
                currentValue,
                xirr * 100, // in %
                cagr * 100  // in %
        );
    }

}