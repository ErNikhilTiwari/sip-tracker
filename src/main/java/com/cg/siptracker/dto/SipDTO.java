package com.cg.siptracker.dto;

import com.cg.siptracker.model.Frequency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SipDTO {

    @NotBlank(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Fund name is required")
    private String fundName;

    @NotBlank(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Start date is required")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Date is in the format yyyy-MM-dd")
    private LocalDate startDate;

    @NotBlank(message = "Frequency is required")
    private Frequency frequency;

}
