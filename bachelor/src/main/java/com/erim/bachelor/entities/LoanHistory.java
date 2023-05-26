package com.erim.bachelor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistory {

    @Id
    @GeneratedValue
    private Long loanHistoryId;
    private LocalDate dateOfLend;
    private LocalDate dateOfReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium_id")
    @JsonIgnoreProperties(value = {"loanHistories"}, allowSetters = true)
    private Medium medium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    @JsonIgnore()
    private Borrower borrower;


    public LoanHistory(LocalDate now,Borrower borrower,Medium medium) {
        this.dateOfLend = now;
        this.borrower = borrower;
        this.medium = medium;
    }
}
