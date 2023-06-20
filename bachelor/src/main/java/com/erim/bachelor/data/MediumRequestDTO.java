package com.erim.bachelor.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediumRequestDTO {

    private Long mediumID;
    private String mediumTyp;
    @Enumerated(EnumType.STRING)
    private String ISBN;
    private String serialNr;
    @ElementCollection // sonst mekert er
    private Set<Integer> year = new HashSet<>();
    @ElementCollection
    private Set<String> subjects = new HashSet<>();
    private double originalPrice;
    private String title;


}
