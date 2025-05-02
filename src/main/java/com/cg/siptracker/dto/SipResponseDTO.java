package com.cg.siptracker.dto;

import com.cg.siptracker.model.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

}
