package com.erim.bachelor.service;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.repositories.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Autowired
    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public List<Borrower> getAllUsers() {
        return borrowerRepository.findAll();
    }

    public Optional<Borrower> getUserById(Long id) {
        return borrowerRepository.findById(id);
    }


    public List<Borrower> getUserByFirstNameAndOrLastName(String firstName, String lastName) {
        return borrowerRepository.searchByFirstAndOrLastName(firstName,lastName);
    }

    public List<Borrower> importUsersCSV(MultipartFile file){

        List<Borrower> result;
        try {
            List<Borrower> borrowers = CSVHelper.csvToUsers(file.getInputStream());
            result = borrowerRepository.saveAll(borrowers);
        }
        catch (IOException e) {
            throw new RuntimeException("fail to store csv data "+e.getMessage());
        }
        return result;
    }

    /**
     * Soft Delete a borrower. The borrower entity will still be in the database but marked as deleted or rather as "left the school".
     * @param borrowerNr The borrower number.
     */
    public void softDeleteBorrowerByNr(Long borrowerNr) {
        Optional<Borrower> toDeleteBorrower = borrowerRepository.findBorrowerByBorrowerNr(borrowerNr);
        if(toDeleteBorrower.isPresent()){
            Borrower borrower = toDeleteBorrower.get();
            borrower.setLeftTheSchool(true);
            borrower.setBorrowerNr(null);
            borrowerRepository.save(borrower);
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Borrower with borrowerNr:"+borrowerNr+" not found");
    }
}
