package com.cg.siptracker.service;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.model.Transaction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AnalyticsService {

    public double calculateCAGR(double investedAmount, double currentValue, long years) {
        if (investedAmount <= 0 || years <= 0) return 0;
        return Math.pow(currentValue / investedAmount, 1.0 / years) - 1;
    }

    public double calculateXIRR(TreeMap<LocalDate, Double> cashFlows) {
        UnivariateFunction irrFunction = rate -> {
            double result = 0.0;
            LocalDate start = cashFlows.firstKey();
            for (Map.Entry<LocalDate, Double> entry : cashFlows.entrySet()) {
                long days = ChronoUnit.DAYS.between(start, entry.getKey());
                result += entry.getValue() / Math.pow(1 + rate, days / 365.0);
            }
            return result;
        };

        try {
            UnivariateSolver solver = new BrentSolver(1e-10);
            return solver.solve(1000, irrFunction, -0.9999, 10);
        } catch (Exception e) {
            return 0; // fallback
        }
    }

    public SipSummaryDto analyzeSIP(SIP sip) {
        double invested = sip.getTransactions().stream()
                .mapToDouble(Transaction::getAmount).sum();

        double totalUnits = sip.getTransactions().stream()
                .mapToDouble(Transaction::getUnits).sum();

        double latestNAV = sip.getLatestNav(); // assume available

        double currentValue = totalUnits * latestNAV;

        long years = ChronoUnit.DAYS.between(
                sip.getStartDate(),
                LocalDate.now()) / 365;

        TreeMap<LocalDate, Double> cashFlows = new TreeMap<>();
        sip.getTransactions().forEach(tx -> {
            cashFlows.put(tx.getDate(), -tx.getAmount());
        });
        cashFlows.put(LocalDate.now(), currentValue); // current redemption value

        double xirr = calculateXIRR(cashFlows);
        double cagr = calculateCAGR(invested, currentValue, years);

        return new SipSummaryDto(
                sip.getFundName(), invested, currentValue,
                xirr * 100, cagr * 100 // in percentage
        );
    }
}
