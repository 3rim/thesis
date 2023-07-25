package com.erim.bachelor.controller;

import com.erim.bachelor.data.AuthenticationRequest;
import com.erim.bachelor.data.AuthenticationResponse;
import com.erim.bachelor.data.PasswordChangeDTO;
import com.erim.bachelor.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/initialLogin")//TODO: define response
    public ResponseEntity<String> changeInitialPassword(@RequestBody PasswordChangeDTO request){
        try {
            authenticationService.changeInitialPassword(request);
            return ResponseEntity.ok("Success");
        }
        catch (Exception e){
           return ResponseEntity.badRequest().body("something went wrong");
        }
    }
}
