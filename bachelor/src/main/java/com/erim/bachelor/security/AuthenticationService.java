package com.erim.bachelor.security;

import com.erim.bachelor.data.AuthenticationRequest;
import com.erim.bachelor.data.AuthenticationResponse;
import com.erim.bachelor.data.Role;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.repositories.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        //user is authenticated
        Borrower borrower = repository.findBorrowerByEmail(request.getEmail()).orElseThrow();
        System.out.println(borrower);
        Set<Role> roleList = borrower.getRoles();
        Map<String,Object> extraClaims = new HashMap<>();
        extraClaims.put("Roles",roleList);
        String jwtToken = jwtService.generateJwt(extraClaims,borrower);
        System.out.println(jwtToken);
        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }
}
