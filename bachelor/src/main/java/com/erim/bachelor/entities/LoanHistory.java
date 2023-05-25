package com.erim.bachelor.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table
public class LoanHistory {

    @Id
    private Long lendHistoryId;
    private LocalDate dateOfLend;
    private LocalDate dateOfReturn;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private Medium medium;

    @ManyToOne
    private Borrower borrower;


}
