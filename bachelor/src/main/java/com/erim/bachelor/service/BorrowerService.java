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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        //TODO: vergleich der File mit dem aktuellen user stand. Die visualisierung soll client seitig sein (Wie password format check)
        List<Borrower> allActiveUsers = getAllActiveUsers();
        try {
            List<Borrower> importedCSVUsers = CSVHelper.csvToUsers(file.getInputStream());
            for (Borrower borrowerCSV: importedCSVUsers) {
                //Suche borrower über die borrowerNr
                Borrower borrower = allActiveUsers.stream()
                        .filter(activeBorrower -> Objects.equals(borrowerCSV.getBorrowerNr(), activeBorrower.getBorrowerNr()))
                        .findAny()
                        .orElse(null); // nicht gefunden ==> neuer borrower

                if(borrower !=null){
                    //User vorhanden ==> updaten
                    updateBorrowerInformation(borrower,borrowerCSV);
                    allActiveUsers.remove(borrower);
                }
                else {
                    //Neuer User
                    borrowerRepository.save(borrowerCSV);
                }
            }
            //übrig gebliebene user löschen
            softDelete(allActiveUsers);
        }
        catch (IOException e) {
            throw new RuntimeException("fail to store csv data "+e.getMessage());
        }
        return null;
    }

    private void softDelete(List<Borrower> inActiveUsers) {
        inActiveUsers.forEach(
                borrower -> {
                    borrower.setBorrowerNr(null);
                    borrower.setLeftTheSchool(true);
                    borrowerRepository.save(borrower);
                });
    }

    private void updateBorrowerInformation(Borrower borrower, Borrower borrowerCSV) {
        borrower.setFirstName(borrowerCSV.getFirstName());
        borrower.setLastName(borrowerCSV.getLastName());

        borrowerRepository.save(borrower);
    }


    /**
     * Returns all Users which did not leave the school.
     * @return A list with active Users (Still on school).
     */
    private List<Borrower> getAllActiveUsers() {
        return borrowerRepository.findAllByBorrowerNrIsNotNull();
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

    public InputStream downloadUsers() {
        return CSVHelper.usersToCSV(borrowerRepository.findAll());
    }
}
