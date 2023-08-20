package com.erim.bachelor.dto;


import com.erim.bachelor.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String jwt;
    private Set<Role> roles;
    private long id;
    private boolean initialLogin;
}
