package com.erim.bachelor.dto;

import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerDTO {

    private Long id;
    private Long borrowerNr;
    private String firstName;
    private String lastName;
    private String borrowerGroup;
    private List<MediumResponse> mediumList;
    private BorrowerState borrowerState;
    //date of birth
    private String dob;
    private Set<Role> roles ;
}
