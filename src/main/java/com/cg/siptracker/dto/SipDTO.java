package com.cg.siptracker.dto;

import com.cg.siptracker.model.Frequency;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SipDTO {

    @NotBlank(message = "Fund name is required")
    private String fundName;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than zero")
    private double amount;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    private Frequency frequency;

}
