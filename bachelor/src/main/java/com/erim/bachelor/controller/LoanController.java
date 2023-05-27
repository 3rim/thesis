package com.erim.bachelor.controller;

import com.erim.bachelor.data.MediumDTO;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.service.LoanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/loan")
public class LoanController {


    private final LoanService loanService;

    private final ModelMapper modelMapper;

    @Autowired
    public LoanController(LoanService loanService, ModelMapper modelMapper) {
        this.loanService = loanService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<List<MediumDTO>> loanMedium(@RequestParam Long userID,
                                                      @RequestParam Long mediumID){
        Borrower borrower = loanService.loanMediumToUser(userID,mediumID);
        List<MediumDTO> response = borrower.getMediumList().
                stream().
                map(this::convertToDTO).
                toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MediumDTO convertToDTO(Medium medium){
        return modelMapper.map(medium,MediumDTO.class);
    }
}
