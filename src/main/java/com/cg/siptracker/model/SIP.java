package com.cg.siptracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "sips")
@Getter
@Setter
@NoArgsConstructor
public class SIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fundName;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private double amount;

    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SIP(Long id, String fundName, Frequency frequency, double amount, LocalDate startDate) {
        this.id = id;
        this.fundName = fundName;
        this.frequency = frequency;
        this.amount = amount;
        this.startDate = startDate;
    }


}