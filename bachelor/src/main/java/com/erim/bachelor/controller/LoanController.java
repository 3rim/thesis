package com.erim.bachelor.controller;

import com.erim.bachelor.dto.LoanHistoriesDTO;
import com.erim.bachelor.dto.MediumRequest;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.LoanHistory;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.service.LoanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "/histories/{mediumID}")
    public ResponseEntity<List<LoanHistoriesDTO>> getLoanHistories(@PathVariable Long mediumID){

        List<LoanHistory> loanHistories = loanService.getLoanHistories(mediumID);
        List<LoanHistoriesDTO> loanHistoriesDTOS = loanHistories.
                stream().
                map(this::convertToDTO).
                toList();
        return new ResponseEntity<>(loanHistoriesDTOS,HttpStatus.OK);
    }

    /**
     * Loan a medium to a borrower or unloan it.
     * @param borrowerID id of borrower
     * @param mediumID id of medium
     * @return A list with all current loaned media to this borrower with borrowerID
     */
    @PostMapping
    public ResponseEntity<List<MediumRequest>> loanMedium(@RequestParam Long borrowerID,
                                                          @RequestParam Long mediumID){
        Borrower borrower = loanService.loanMediumToUser(borrowerID,mediumID);
        List<MediumRequest> borrowerLoanedMedia = borrower.getMediumList().
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
    private MediumRequest convertToDTO(Medium medium){
        return modelMapper.map(medium, MediumRequest.class);
    }

    /**
     * Converts a LoanHistory into a DTO
     * @param loanHistory the loanHistory to be converted into DTO
     * @return LoanHistoryDTO
     */
    private LoanHistoriesDTO convertToDTO(LoanHistory loanHistory){
        //model mapper throws infinite loop exception, idk why

        LoanHistoriesDTO dto = new LoanHistoriesDTO();
        dto.setLoanHistoryId(loanHistory.getLoanHistoryId());
        dto.setMedium(loanHistory.getMedium().getMediaSeries().getTitle());
        dto.setDateOfLend(loanHistory.getDateOfLend());
        dto.setDateOfReturn(loanHistory.getDateOfReturn());

        if(loanHistory.getBorrower() !=null)
            dto.setBorrower(loanHistory.getBorrower().getFullName());

        return dto;
    }
}
