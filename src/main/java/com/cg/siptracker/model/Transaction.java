package com.cg.siptracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private LocalDate date;

    @Setter
    private double amount;

    private double nav;

    private double units;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sip_id")
    private SIP sip;

    // Constructors
    public Transaction() {}

    public Transaction(LocalDate date, double amount, double nav, SIP sip) {
        this.date = date;
        this.amount = amount;
        this.nav = nav;
        this.units = amount / nav;
        this.sip = sip;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public LocalDate getDate() { return date; }

    public double getAmount() { return amount; }

    public double getNav() { return nav; }

    public double getUnits() { return units; }

    public SIP getSip() { return sip; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", amount=" + amount +
                ", nav=" + nav +
                ", units=" + units +
                '}';
    }
}
