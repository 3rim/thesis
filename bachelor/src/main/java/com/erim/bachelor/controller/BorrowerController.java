package com.erim.bachelor.controller;

import com.erim.bachelor.dto.BorrowerDTO;
import com.erim.bachelor.dto.InitBorrowerDTO;
import com.erim.bachelor.dto.MediumResponse;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.enums.Role;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.service.BorrowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(path = "api/v1/borrowers")
@Tag(name = "Borrowers")
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
     * Get Borrowers by Page. Pageindex starts at 0.
     * Code reference: <a href="https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/">bezkoder</a>
     * @return Page of Borrowers
     */
    @Operation(summary = "Get Pageable borrowers",description = """
            Get borrowers in small chunks(by Page).
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully loaned/unloaned",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    examples =  @ExampleObject(name = "Pageable Response", value = """
                                            {
                                                "totalItems": 5,
                                                "borrowers": [
                                                    {
                                                        "id": 2,
                                                        "borrowerNr": 101,
                                                        "firstName": "admin",
                                                        "lastName": "admin",
                                                        "borrowerGroup": null,
                                                        "mediumList": [],
                                                        "leftTheSchool": false,
                                                        "borrowerState": "ACTIVE",
                                                        "dob": "2023-09-06",
                                                        "roles": [
                                                            "ADMIN"
                                                        ]
                                                    },
                                                    {
                                                        "id": 1,
                                                        "borrowerNr": 100,
                                                        "firstName": "user",
                                                        "lastName": "user",
                                                        "borrowerGroup": null,
                                                        "mediumList": [],
                                                        "leftTheSchool": false,
                                                        "borrowerState": "ACTIVE",
                                                        "dob": "2023-09-06",
                                                        "roles": [
                                                            "USER"
                                                        ]
                                                    }
                                                ],
                                                "totalPages": 2,
                                                "currentPage": 1
                                            }
                                            """))
                    }),
    })
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String,Object >> getPageableBorrowers(
            @RequestParam(required = false) BorrowerState borrowerState,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "borrowerID,desc")String[] sort
            ){
        try {
            List<Order> orders = new ArrayList<>();
            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Borrower> borrowers;
            //Create pageSort by page,size and given sorts in orders
            Pageable pageable = PageRequest.of(page, pageSize,Sort.by(orders));
            Page<Borrower> pageBorrowers;
            boolean filterByName = firstName !=null || lastName != null;
            //No Filter => getAll
            if(borrowerState == null && !filterByName)
                pageBorrowers = borrowerService.getUsers(pageable);
            //state and name
            else if(borrowerState != null && filterByName )
                pageBorrowers = borrowerService.getAllByStateAndName(pageable,borrowerState,firstName,lastName);
            //name
            else if(filterByName)
                pageBorrowers = borrowerService.getAllByName(pageable,firstName,lastName);
            //state
            else
                pageBorrowers = borrowerService.getByState(pageable,borrowerState);

            borrowers = pageBorrowers.getContent();
            if(borrowers.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<BorrowerDTO> borrowerDTOS = borrowers
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
            //Create Response
            Map<String,Object> response = new HashMap<>();
            response.put("borrowers",borrowerDTOS);
            response.put("currentPage", pageBorrowers.getNumber());
            response.put("totalItems", pageBorrowers.getTotalElements());
            response.put("totalPages", pageBorrowers.getTotalPages());
            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Get Borrower by id
     * @param id id of Borrower
     * @return The Borrowers or a Not found exception
     */
    @Operation(summary = "Get Borrower by id", description ="Returns borrowerDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - Borrower not found",content = @Content)
    })
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
    @Operation(summary = "Get Borrowers by first and/or lastname", description ="Returns List of borrowerDTO that matches query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping(path = "name")
    public List<BorrowerDTO>getBorrowersByName(
            @RequestParam(required = false)String firstName,
            @RequestParam(required = false)String lastName){
        List<Borrower> listBorrowers = borrowerService.getUserByFirstNameAndOrLastName(firstName,lastName);

        //map each Borrower -> BorrowerDTO, store to list and return the list
        return listBorrowers.stream().map(borrower -> modelMapper.map(borrower, BorrowerDTO.class)).toList();
    }

    @Operation(summary = "Get Borrower.csv", description ="Returns csv file with borrowers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",content = @Content),
    })
    @GetMapping(path = "/csvFile",produces="text/csv")
    public ResponseEntity<Resource> getCSVFile(){
        String filename = "users_"+LocalDate.now()+".csv";
        InputStreamResource file = new InputStreamResource(borrowerService.downloadUsers());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);

    }

    @Operation(summary = "Import User csv", description = """
            New users are added/updated/deleted via csv import
            
            User in csv not in database => new user
            
            User in csv and in database => update if needed user
            
            User not in csv bit in database => deactivate user
                        
            CSV-Schema
            
            id	firstName	lastName	group	DateOfBirth	Email
            
            1	August	    Dowyer	    5a	    12.06.2000	august.dowyer@schulnetz-gsm.de
                  
            Returns csv file with new added users and their oneTimePassword
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved", content =@Content),
            @ApiResponse(responseCode = "417", description = "EXPECTATION_FAILED -  file is incorrect",content = @Content),
    })
    @PostMapping()
    public ResponseEntity<List<InitBorrowerDTO>> importCSV(
            @RequestParam("file")  MultipartFile file){
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

    @Operation(summary = "Edit user roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited"),
            @ApiResponse(responseCode = "404", description = "Not found - Borrower not found",content = @Content)
    })
    @PatchMapping(path ="{id}/roles")
    public ResponseEntity<String> editUserRoles(@PathVariable(value = "id")Long id,@RequestBody Set<Role> roles){
        try {
            borrowerService.updateBorrowerRoles(id,roles);
            return new ResponseEntity<>("roles updated for id:" +id, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Server Error:Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "reset passwords",description = "returns csv file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edited"),
    })
    @PutMapping(path = "/reset")
    public ResponseEntity<List<InitBorrowerDTO>> resetPasswords (@RequestBody List<Long> ids){
        List<InitBorrowerDTO>  response = borrowerService.resetPasswords(ids);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * Deletes Borrowers by ID if the borrower has no lend media
     * @param ids The BorrowerIDs to be deleted
     */
    @Operation(summary = "delete borrowers by their ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
    })
    @DeleteMapping()
    public void deleteBorrowersById(@RequestBody List<Long> ids){
        borrowerService.deleteBorrowersByID(ids);
    }

    /**
     * Converts a Borrower to BorrowerDTO
     * @param borrower the borrower
     * @return BorrowerDTO
     */
    private BorrowerDTO convertToDTO(Borrower borrower){
        List<MediumResponse> borrowerLendMedia = borrower.getMediumList().
                stream().
                map(medium -> {
                    MediumResponse mediumResponse = modelMapper.map(medium,MediumResponse.class);
                    mediumResponse.setTitle(medium.getMediaSeries().getTitle());
                    mediumResponse.setCurrentBorrower(borrower.getFullName());
                    return mediumResponse;
                }).
                toList();

        BorrowerDTO borrowerDTO = modelMapper.map(borrower, BorrowerDTO.class);
        borrowerDTO.setMediumList(borrowerLendMedia);

        return borrowerDTO;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
}
