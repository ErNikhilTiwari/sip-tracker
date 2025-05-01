package com.cg.siptracker.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SipSummaryDto {

    private String fundName;
    private double investedAmount;
    private double currentValue;
    private double xirr; // in percentage (e.g. 12.5 means 12.5%)
    private double cagr; // in percentage

    public SipSummaryDto() {
    }

    public SipSummaryDto(String fundName, double investedAmount, double currentValue, double xirr, double cagr) {
        this.fundName = fundName;
        this.investedAmount = investedAmount;
        this.currentValue = currentValue;
        this.xirr = xirr;
        this.cagr = cagr;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public void setInvestedAmount(double investedAmount) {
        this.investedAmount = investedAmount;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public void setXirr(double xirr) {
        this.xirr = xirr;
    }

    public void setCagr(double cagr) {
        this.cagr = cagr;
    }

    @Override
    public String toString() {
        return "SIPSummaryDTO{" +
                "fundName='" + fundName + '\'' +
                ", investedAmount=" + investedAmount +
                ", currentValue=" + currentValue +
                ", xirr=" + xirr +
                ", cagr=" + cagr +
                '}';
    }
}

