package com.erim.bachelor.controller;

import com.erim.bachelor.data.InitBorrowerDTO;
import com.erim.bachelor.data.MediumRequestDTO;
import com.erim.bachelor.data.BorrowerDTO;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.service.BorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/borrowers")
@CrossOrigin
public class BorrowerController {
    private final BorrowerService borrowerService;
    private final ModelMapper modelMapper;
    @Autowired
    public BorrowerController(BorrowerService borrowerService, ModelMapper modelMapper){
        this.borrowerService = borrowerService;
        this.modelMapper = modelMapper;
    }

    /**
     * Get all Borrowers
     *
     * @return A List which is either empty or contains all Borrowers
     */
    @GetMapping()
    public List<Borrower> getAllBorrowers(){return borrowerService.getAllUsers();}

    /**
     * Get Borrower by id
     * @param id id of Borrower
     * @return The Borrowers or a Not found exception
     */
    @GetMapping(path = "{id}")
    @PreAuthorize("#id == authentication.principal.borrowerID or hasAnyAuthority('ADMIN','LIBRARIAN','LOAN_HELPER')")
    public ResponseEntity<BorrowerDTO> getBorrowerById(@PathVariable( value = "id") Long id){
        Optional<Borrower> borrower = borrowerService.getUserById(id);
        if(borrower.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Borrower with id:"+id+" not found");

        return new ResponseEntity<>(convertToDTO(borrower.get()),HttpStatus.OK);
    }

    /**
     * Search for Borrowers by first and or last name.
     * Returns an empty list if no match is found otherwise all matches
     *
     * @param firstName first name of Borrowers
     * @param lastName last name of Borrowers
     * @return A list with matched Borrowers or an empty list
     */
    @GetMapping(path = "byName")
    public List<BorrowerDTO>getBorrowersByName(
            @RequestParam(required = false)String firstName,
            @RequestParam(required = false)String lastName){
        List<Borrower> listBorrowers = borrowerService.getUserByFirstNameAndOrLastName(firstName,lastName);

        //map each Borrower -> BorrowerDTO, store to list and return the list
        return listBorrowers.stream().map(borrower -> modelMapper.map(borrower, BorrowerDTO.class)).toList();
    }

    @GetMapping(path = "/csvFile")
    public ResponseEntity<Resource> getCSVFile(){
        String filename = "users_"+LocalDate.now()+".csv";
        InputStreamResource file = new InputStreamResource(borrowerService.downloadUsers());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);

    }

    @PostMapping()
    public ResponseEntity<List<InitBorrowerDTO>> importCSV(@RequestParam("file") MultipartFile file){
        if(CSVHelper.hasCSVFormat(file)){
            try{
                List<InitBorrowerDTO> result= borrowerService.importUsersCSV(file);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }catch (Exception e){
                throw new ResponseStatusException(
                        HttpStatus.EXPECTATION_FAILED, "file is not correct");
            }
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Please upload a file");
    }

    @PutMapping(path ="{id}", consumes = "application/json",produces = "application/json")
    public void editUserRoles(@PathVariable(value = "id")Long id,@RequestBody Borrower borrower){
        //TODO: implement
    }

    /**
     * Delete a Borrower by his Number (Not EntityID).
     * @param borrowerNr The BorrowerNumber
     */
    @DeleteMapping(value = "{borrowerNr}")
    public void softDeleteBorrowerByNr(@PathVariable(value = "borrowerNr")Long borrowerNr){
        borrowerService.softDeleteBorrowerByNr(borrowerNr);
    }

    /**
     * Converts a Borrower to BorrowerDTO
     * @param borrower the borrower
     * @return BorrowerDTO
     */
    private BorrowerDTO convertToDTO(Borrower borrower){
        List<MediumRequestDTO> borrowerLendMedia = borrower.getMediumList().
                stream().
                map(medium -> modelMapper.map(medium, MediumRequestDTO.class)).
                toList();

        BorrowerDTO borrowerDTO = modelMapper.map(borrower, BorrowerDTO.class);
        borrowerDTO.setMediumList(borrowerLendMedia);


        return borrowerDTO;
    }
}
