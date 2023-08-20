package com.erim.bachelor.dto;

import com.erim.bachelor.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediumResponse {

    private Long mediumID;
    private Long mediaSeriesID;
    private String serialNr;
    private String title;
    private String currentBorrower;
    @Enumerated(EnumType.STRING)
    private Status status;
    //Currently lend since
    private LocalDate dateOfLend;

}
