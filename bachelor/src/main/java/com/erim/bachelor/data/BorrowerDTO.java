package com.erim.bachelor.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerDTO {

    private Long id;
    private Long borrowerNr;
    private String firstName;
    private String lastName;
    private String borrowerGroup;
    private List<MediumRequestDTO> mediumList;
    private boolean leftTheSchool;
    private String dob;
}
