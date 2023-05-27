package com.erim.bachelor.data;

import com.erim.bachelor.entities.Medium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediumDTO {

    private Long id;
    private String title;
    private String serialNr;


}
