package com.erim.bachelor.data;

import com.erim.bachelor.entities.LoanHistory;
import com.erim.bachelor.enums.Status;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediumResponseDTO {

    private Long mediumID;
    private String mediumTyp;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String ISBN;
    private String serialNr;
    @ElementCollection
    private Set<Integer> year = new HashSet<>();
    @ElementCollection
    private Set<String> subjects = new HashSet<>();
    private double originalPrice;
    private String title;
    private String currentBorrower;
    //Currently lend since
    private LocalDate dateOfLend;
    //private List<LoanHistory> loanHistories;
}
