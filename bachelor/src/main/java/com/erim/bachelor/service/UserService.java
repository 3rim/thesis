package com.erim.bachelor.service;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Borrower> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Borrower> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public List<Borrower> getUserByFirstNameAndOrLastName(String firstName, String lastName) {
        return userRepository.searchByFirstAndOrLastName(firstName,lastName);
    }

    public List<Borrower> addUsers(MultipartFile file){

        List<Borrower> result;
        try {
            List<Borrower> borrowers = CSVHelper.csvToUsers(file.getInputStream());
            result = userRepository.saveAll(borrowers);
        }
        catch (IOException e) {
            throw new RuntimeException("fail to store csv data "+e.getMessage());
        }
        return result;
    }
}
