package com.erim.bachelor.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediumRequest {
    private Long mediumID;
    private String serialNr;
}
