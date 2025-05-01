package com.cg.siptracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "sips")
public class SIP {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fundName;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private double amount;

    private LocalDate startDate;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private double latestNav;

    @Setter
    @OneToMany(mappedBy = "sip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // Constructors
    public SIP() {}

    public SIP(String fundName, Frequency frequency, double amount, LocalDate startDate, User user, double latestNav) {
        this.fundName = fundName;
        this.frequency = frequency;
        this.amount = amount;
        this.startDate = startDate;
        this.user = user;
        this.latestNav = latestNav;
    }

    @Override
    public String toString() {
        return "SIP{" +
                "id=" + id +
                ", fundName='" + fundName + '\'' +
                ", amount=" + amount +
                ", startDate=" + startDate +
                ", latestNav=" + latestNav +
                '}';
    }

}