package com.erim.bachelor.controller;

import com.erim.bachelor.data.InitBorrowerDTO;
import com.erim.bachelor.data.MediumRequestDTO;
import com.erim.bachelor.data.BorrowerDTO;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.service.BorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<Map<String,Object >> getAllBorrowers(
            @RequestParam(required = false) BorrowerState borrowerState,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
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
            Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
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
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


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
    @GetMapping(path = "name")
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

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
}
