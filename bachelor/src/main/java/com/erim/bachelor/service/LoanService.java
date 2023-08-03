package com.erim.bachelor.service;

import com.erim.bachelor.enums.Status;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.LoanHistory;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import com.erim.bachelor.repositories.LoanHistoryRepository;
import com.erim.bachelor.repositories.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class LoanService {

    private final BorrowerRepository borrowerRepository;
    private final InventoryRepository inventoryRepository;

    private final LoanHistoryRepository loanHistoryRepository;

    @Autowired
    public LoanService(BorrowerRepository borrowerRepository, InventoryRepository inventoryRepository, LoanHistoryRepository loanHistoryRepository) {
        this.borrowerRepository = borrowerRepository;
        this.inventoryRepository = inventoryRepository;
        this.loanHistoryRepository = loanHistoryRepository;
    }

    public Borrower loanMediumToUser(Long userID, Long mediumID) {
        Borrower borrower;
        Medium medium;

        if(borrowerRepository.findById(userID).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id: "+userID+" not found");
        else
            borrower = borrowerRepository.findById(userID).get();

        if(inventoryRepository.findById(mediumID).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "medium with id: "+mediumID+" not found");
        else
            medium = inventoryRepository.findById(mediumID).get();

        if(medium.isBorrowed()){
            //Borrower != medium.getBorrower
            if(!Objects.equals(medium.getBorrower().getBorrowerID(), borrower.getBorrowerID()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "medium with id:"+mediumID+ " already loaned to "+medium.getBorrower().getFullName());
            else{ // Medium is already loan to this borrower ==> take it back from him
                return unloanFromUser(borrower,medium);
            }
        }
        else {
            return loanToUser(borrower,medium);
        }

    }

    private Borrower unloanFromUser(Borrower borrower, Medium medium) {
        medium.setStatus(Status.AVAILABLE);
        medium.setBorrower(null);
        medium.setDateOfLend(null);
        /*Optional<LoanHistory> lh = loanHistoryRepository.findLoanHistoryByMediumMediumID(medium.getMediumID());
        LoanHistory loanHistory =lh.get();
        loanHistory.setDateOfReturn(LocalDate.now());
        loanHistoryRepository.save(loanHistory);*/

        List<LoanHistory> listLoanHistories = medium.getLoanHistories();
        LoanHistory loanHistory = medium.getLoanHistories().get(medium.getLoanHistories().size()-1);
        Long loanHistoryID= loanHistory.getLoanHistoryId();
        loanHistory.setDateOfReturn(LocalDate.now());
        //loanHistoryRepository.save(loanHistory);
        inventoryRepository.save(medium);

        return  borrower;
    }

    private Borrower loanToUser(Borrower borrower, Medium medium) {
        medium.setStatus(Status.RENT);
        medium.setBorrower(borrower);
        LocalDate now = LocalDate.now();
        medium.setDateOfLend(now);

        LoanHistory loanHistory = new LoanHistory(now,borrower,medium);
        medium.addNewLoanHistory(loanHistory);
        inventoryRepository.save(medium);

        return  borrower;
    }

    public List<LoanHistory> getLoanHistories(Long mediumID) {
        return loanHistoryRepository.findAllLoanHistoryByMediumMediumID(mediumID);
    }
}

