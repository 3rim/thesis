package com.erim.bachelor.controller;

import com.erim.bachelor.data.AuthenticationRequest;
import com.erim.bachelor.data.AuthenticationResponse;
import com.erim.bachelor.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        //
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
