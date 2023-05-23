package com.erim.bachelor.controller;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Get all Users
     *
     * @return A List which is either empty or contains Users
     */
    @GetMapping(path = "all")
    public List<Borrower> getAllUser(){
        return userService.getAllUsers();
    }

    /**
     * Get user by id
     * @param id id of user
     * @return The user or a Not found exeption
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<Borrower> getUserById(@PathVariable( value = "id") Long id){
        Optional<Borrower> borrower = userService.getUserById(id);
        if(borrower.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");

        return new ResponseEntity<>(borrower.get(),HttpStatus.OK);
    }

    /**
     * Search for User by first and or last name.
     * An empty list if no match or a list with all matches
     *
     * @param firstName first name of User
     * @param lastName last name of user
     * @return A list with matched users or an empty list
     */
    @GetMapping
    public List<Borrower>getUsersByName(
            @RequestParam(required = false)String firstName,
            @RequestParam(required = false)String lastName){

        System.out.println(firstName);
        System.out.println(lastName);
        return userService.getUserByFirstNameAndOrLastName(firstName,lastName);

    }

    @PostMapping()
    public ResponseEntity<List<Borrower>> addUser(@RequestParam("file") MultipartFile file){
        System.out.println("ererer");
        if(CSVHelper.hasCSVFormat(file)){
            try{
                List<Borrower> result= userService.addUsers(file);
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

    }



}
