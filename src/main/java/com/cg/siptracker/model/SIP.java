package com.cg.siptracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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