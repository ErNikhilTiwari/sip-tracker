package com.cg.siptracker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "sips")
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

}