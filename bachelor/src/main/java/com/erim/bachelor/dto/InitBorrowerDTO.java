package com.erim.bachelor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Borrower DTO for new added Borrowers
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitBorrowerDTO {

    private Long borrowerNr;
    private String firstName;
    private String lastName;
    private String borrowerGroup;
    private String email;
    private String oneTimePassword;
    private String dob;
}
