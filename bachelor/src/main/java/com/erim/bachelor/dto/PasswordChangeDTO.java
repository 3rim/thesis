package com.erim.bachelor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {
    private String email;
    private String currentPassword;
    private String newPassword;
}
