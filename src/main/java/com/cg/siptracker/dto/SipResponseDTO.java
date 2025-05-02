package com.cg.siptracker.dto;

import com.cg.siptracker.model.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SipResponseDTO {
    private Long id;
    private String fundName;
    private Double amount;
    private Frequency frequency;
    private LocalDate startDate;
    private double xirr;
    private double cagr;

    public  SipResponseDTO(Long id, String fundName, Double amount, Frequency frequency, LocalDate startDate) {
        this.id = id;
        this.fundName = fundName;
        this.amount = amount;
        this.frequency = frequency;
        this.startDate = startDate;
    }

}
