package com.erim.bachelor.controller;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/loan")
public class LoanController {


    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Borrower> loanMedium(@RequestParam Long userID,
                                               @RequestParam Long mediumID){
        Borrower borrower = loanService.loanMediumToUser(userID,mediumID);


        return new ResponseEntity<>(borrower, HttpStatus.OK);
    }
}
