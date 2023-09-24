package com.erim.bachelor.controller;

import com.erim.bachelor.dto.LoanHistoriesDTO;
import com.erim.bachelor.dto.MediumRequest;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.LoanHistory;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.exceptions.MediumIsBorrowedException;
import com.erim.bachelor.service.ILoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/v1/loan")
@Tag(name = "Loaning")
public class LoanController {
    private final ILoanService loanService;
    private final ModelMapper modelMapper;
    @Autowired
    public LoanController(ILoanService loanService, ModelMapper modelMapper) {
        this.loanService = loanService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get LoanHistories for a Medium by MediumID", description = "Returns List of LoanHistory of a Medium")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
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
    @Operation(summary = "Loan or unloan Medium",
            description =
                    """
                            If Medium is not loaned by borrower => loan
                            
                            If Medium is loaned by borrower => unloan
                            
                            Returns List of Medium loaned by borrower""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully loaned/unloaned"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "406", description = "Bad Request - Medium is already borrowed by another borrower",content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - Borrower/Medium not found",content = @Content)
    })
    @PostMapping
    public ResponseEntity<List<MediumRequest>> loanMedium(@RequestParam Long borrowerID,
                                                          @RequestParam Long mediumID) {
        Borrower borrower;
        try {
            borrower = loanService.loanUnloanMedium(borrowerID,mediumID);
            List<MediumRequest> borrowerLoanedMedia = borrower.getMediumList().
                    stream().
                    map(this::convertToDTO).
                    toList();

            return new ResponseEntity<>(borrowerLoanedMedia, HttpStatus.OK);
        } catch (MediumIsBorrowedException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        }
        catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"something went wrong :(");
        }
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
        //model mapper throws infinite loop exception, IDK why

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
