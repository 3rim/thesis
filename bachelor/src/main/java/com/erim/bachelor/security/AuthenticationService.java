package com.erim.bachelor.security;

import com.erim.bachelor.dto.*;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.enums.Role;
import com.erim.bachelor.repositories.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BorrowerRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        //user is authenticated
        Borrower borrower = repository.findBorrowerByEmail(request.getEmail()).orElseThrow();
        //Check for initialLogin
        if(borrower.getBorrowerState().equals(BorrowerState.INITIALIZED)){
            return AuthenticationResponse.builder()
                    .initialLogin(true)
                    .build();
        }
        else {
            Set<Role> roleSet = borrower.getRoles();
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("Roles", roleSet);
            String jwtToken = jwtService.generateJwt(extraClaims, borrower);
            return AuthenticationResponse.builder()
                    .jwt(jwtToken)
                    .roles(roleSet)
                    .id(borrower.getBorrowerID())
                    .build();
        }
    }

    public void changeInitialPassword(PasswordChangeDTO request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getCurrentPassword()));
        Borrower borrower = repository.findBorrowerByEmail(request.getEmail()).orElseThrow();
        borrower.setPassword(passwordEncoder.encode(request.getNewPassword()));
        borrower.setBorrowerState(BorrowerState.ACTIVE);
        repository.save(borrower);
    }
}
