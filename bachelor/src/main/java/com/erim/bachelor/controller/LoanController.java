package com.erim.bachelor.controller;

import com.erim.bachelor.data.MediumRequestDTO;
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

    /**
     * Loan a medium to a borrower or unloan it.
     * @param borrowerID id of borrower
     * @param mediumID id of medium
     * @return A list with all current loaned media to this borrower with borrowerID
     */
    @PostMapping
    public ResponseEntity<List<MediumRequestDTO>> loanMedium(@RequestParam Long borrowerID,
                                                             @RequestParam Long mediumID){
        Borrower borrower = loanService.loanMediumToUser(borrowerID,mediumID);
        List<MediumRequestDTO> borrowerLoanedMedia = borrower.getMediumList().
                stream().
                map(this::convertToDTO).
                toList();

        return new ResponseEntity<>(borrowerLoanedMedia, HttpStatus.OK);
    }

    /**
     * Converts a Medium into MediumDTO
     * @param medium the Medium to be converted into MediumDTO
     * @return MediumDTO
     */
    private MediumRequestDTO convertToDTO(Medium medium){
        return modelMapper.map(medium, MediumRequestDTO.class);
    }
}
