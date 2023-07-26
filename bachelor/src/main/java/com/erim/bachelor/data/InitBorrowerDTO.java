package com.erim.bachelor.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
