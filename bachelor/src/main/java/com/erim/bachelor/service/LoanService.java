package com.erim.bachelor.service;

import com.erim.bachelor.data.Status;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import com.erim.bachelor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LoanService {

    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public LoanService(UserRepository userRepository, InventoryRepository inventoryRepository) {
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Borrower loanMediumToUser(Long userID, Long mediumID) {
        Borrower borrower;
        Medium medium;

        if(userRepository.findById(userID).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id: "+userID+" not found");
        else
            borrower = userRepository.findById(userID).get();

        if(inventoryRepository.findById(mediumID).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "medium with id: "+mediumID+" not found");
        else
            medium = inventoryRepository.findById(mediumID).get();

        if(medium.isBorrowed()){
            //Borrower != medium.getBorrower
            if(!Objects.equals(medium.getBorrower().getBorrowerID(), borrower.getBorrowerID()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "medium with id:"+mediumID+ " already loaned to "+medium.getBorrower().getFullName());
            else // Medium is already loan to this borrower ==> take it back from him
                return unloanFromUser(borrower,medium);
        }
        else {
            return loanToUser(borrower,medium);
        }

    }

    private Borrower unloanFromUser(Borrower borrower, Medium medium) {
        medium.setStatus(Status.AVAILABLE);
        medium.setBorrower(null);
        inventoryRepository.save(medium);

        return  borrower;
    }

    private Borrower loanToUser(Borrower borrower, Medium medium) {
        medium.setStatus(Status.RENT);
        medium.setBorrower(borrower);
        inventoryRepository.save(medium);

        return  borrower;
    }
}

