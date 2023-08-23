package com.erim.bachelor.controller;

import com.erim.bachelor.dto.AuthenticationRequest;
import com.erim.bachelor.dto.AuthenticationResponse;
import com.erim.bachelor.dto.PasswordChangeDTO;
import com.erim.bachelor.security.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authentication")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        try {
            //
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
