package com.cg.siptracker.service;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.Frequency;
import com.cg.siptracker.model.NAVRecord;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.NAVRecordRepository;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.TreeMap;

@Service
public class AnalyticsService {

    @Autowired
    private NAVRecordRepository navRecordRepository;

    public SipSummaryDto analyzeSIP(SIP sip) {
        LocalDate today = LocalDate.now();
        LocalDate date = sip.getStartDate();
        double amountPerInstallment = sip.getAmount();
//        double latestNav = sip.getLatestNav();

        double latestNav = navRecordRepository.findLatestByFundName(sip.getFundName())
                .map(NAVRecord::getNavValue)
                .orElseThrow(() -> new RuntimeException("No NAV found for " + sip.getFundName()));


        TreeMap<LocalDate, Double> cashFlows = new TreeMap<>();
        double totalUnits = 0;
        double totalInvested = 0;

        while (!date.isAfter(today)) {
            cashFlows.put(date, -amountPerInstallment);
            totalInvested += amountPerInstallment;
            totalUnits += amountPerInstallment / latestNav; // assume latest NAV was constant
            date = incrementDate(date, sip.getFrequency());
        }

        double currentValue = totalUnits * latestNav;
        cashFlows.put(today, currentValue);

        long years = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(sip.getStartDate(), today) / 365);

        double xirr = calculateXIRR(cashFlows);
        double cagr = calculateCAGR(totalInvested, currentValue, years);

        return new SipSummaryDto(
                sip.getFundName(),
                totalInvested,
                currentValue,
                xirr * 100,
                cagr * 100
        );
    }

    private LocalDate incrementDate(LocalDate date, Frequency frequency) {
        return switch (frequency) {
//            case DAILY -> date.plusDays(1);
            case WEEKLY -> date.plusWeeks(1);
            case MONTHLY -> date.plusMonths(1);
//            case QUARTERLY -> date.plusMonths(3);
//            case YEARLY -> date.plusYears(1);
        };
    }

    private double calculateCAGR(double invested, double current, long years) {
        if (invested <= 0 || years <= 0) return 0;
        return Math.pow(current / invested, 1.0 / years) - 1;
    }

    private double calculateXIRR(TreeMap<LocalDate, Double> cashFlows) {
        UnivariateFunction irrFunction = rate -> {
            double result = 0.0;
            LocalDate start = cashFlows.firstKey();
            for (var entry : cashFlows.entrySet()) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(start, entry.getKey());
                result += entry.getValue() / Math.pow(1 + rate, days / 365.0);
            }
            return result;
        };

        try {
            UnivariateSolver solver = new BrentSolver(1e-10);
            return solver.solve(1000, irrFunction, -0.9999, 10);
        } catch (Exception e) {
            return 0;
        }
    }
}





















//package com.cg.siptracker.service;
//
//import com.cg.siptracker.dto.SipSummaryDto;
//import com.cg.siptracker.model.Frequency;
//import com.cg.siptracker.model.NAVRecord;
//import com.cg.siptracker.model.SIP;
//import com.cg.siptracker.repository.NAVRecordRepository;
//import org.apache.commons.math3.analysis.UnivariateFunction;
//import org.apache.commons.math3.analysis.solvers.BrentSolver;
//import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.*;
//import java.time.temporal.ChronoUnit;
//
//@Service
//public class AnalyticsService {
//
//    @Autowired
//    private NAVRecordRepository navRecordRepository;
//
//    public SipSummaryDto analyzeSIP(SIP sip) {
//        LocalDate start = sip.getStartDate();
//        LocalDate today = LocalDate.now();
//
//        // Fetch all NAVs for this fund, ordered by date
//        List<NAVRecord> navRecords = navRecordRepository.findByFundNameOrderByDateAsc(sip.getFundName());
//        if (navRecords.isEmpty()) {
//            throw new RuntimeException("No NAV records found for fund: " + sip.getFundName());
//        }
//
//        // Build a date-to-NAV map
//        Map<LocalDate, Double> navMap = new TreeMap<>();
//        for (NAVRecord nav : navRecords) {
//            navMap.put(nav.getDate(), nav.getNavValue());
//        }
//
//        double totalInvested = 0.0;
//        double totalUnits = 0.0;
//        TreeMap<LocalDate, Double> cashFlows = new TreeMap<>();
//
//        // Simulate SIP investments
//        LocalDate current = start;
//        while (!current.isAfter(today)) {
//            Double nav = navMap.get(current);
//            if (nav != null) {
//                totalInvested += sip.getAmount();
//                totalUnits += sip.getAmount() / nav;
//                cashFlows.put(current, -sip.getAmount());
//            }
//            current = incrementDate(current, sip.getFrequency());
//        }
//
//        // Get latest NAV
//        NAVRecord latest = navRecords.get(navRecords.size() - 1);
//        double currentValue = totalUnits * latest.getNavValue();
//        cashFlows.put(today, currentValue);
//
//        long totalDays = ChronoUnit.DAYS.between(start, today);
//        long years = Math.max(1, totalDays / 365);
//
//        double xirr = calculateXIRR(cashFlows);
//        double cagr = calculateCAGR(totalInvested, currentValue, years);
//
//        return new SipSummaryDto(
//                sip.getFundName(),
//                totalInvested,
//                currentValue,
//                xirr * 100,
//                cagr * 100
//        );
//    }
//
//    private LocalDate incrementDate(LocalDate date, Frequency frequency) {
//        return switch (frequency) {
////            case DAILY -> date.plusDays(1);
//            case WEEKLY -> date.plusWeeks(1);
//            case MONTHLY -> date.plusMonths(1);
////            case QUARTERLY -> date.plusMonths(3);
////            case YEARLY -> date.plusYears(1);
//        };
//    }
//
//
//    private double calculateCAGR(double invested, double current, long years) {
//        if (invested <= 0 || years <= 0) return 0;
//        return Math.pow(current / invested, 1.0 / years) - 1;
//    }
//
//    private double calculateXIRR(TreeMap<LocalDate, Double> cashFlows) {
//        UnivariateFunction irrFunction = rate -> {
//            double result = 0.0;
//            LocalDate start = cashFlows.firstKey();
//            for (Map.Entry<LocalDate, Double> entry : cashFlows.entrySet()) {
//                long days = ChronoUnit.DAYS.between(start, entry.getKey());
//                result += entry.getValue() / Math.pow(1 + rate, days / 365.0);
//            }
//            return result;
//        };
//
//        try {
//            UnivariateSolver solver = new BrentSolver(1e-10);
//            return solver.solve(1000, irrFunction, -0.9999, 10);
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//}
