package com.erim.bachelor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistoriesDTO {

    private Long loanHistoryId;
    private LocalDate dateOfLend;
    private LocalDate dateOfReturn;
    private String borrower;
    private String medium;
}
