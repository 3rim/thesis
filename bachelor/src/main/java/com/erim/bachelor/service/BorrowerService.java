package com.erim.bachelor.service;

import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.data.InitBorrowerDTO;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.helper.PasswordGenerator;
import com.erim.bachelor.repositories.BorrowerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public BorrowerService(BorrowerRepository borrowerRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.borrowerRepository = borrowerRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public Page<Borrower> getAllUsers(Pageable pageable) {
        System.out.println("service ");
        return borrowerRepository.findAll(pageable);
    }

    public Optional<Borrower> getUserById(Long id) {
        return borrowerRepository.findById(id);
    }


    public List<Borrower> getUserByFirstNameAndOrLastName(String firstName, String lastName) {
        return borrowerRepository.searchByFirstAndOrLastName(firstName,lastName);
    }

    public List<InitBorrowerDTO> importUsersCSV(MultipartFile file){
        List<Borrower> allActiveUsers = getAllActiveUsers();
        List<InitBorrowerDTO> newBorrowerDTOs = new ArrayList<>();
        try {
            List<Borrower> importedCSVUsers = CSVHelper.csvToUsers(file.getInputStream());
            for (Borrower borrowerCSV: importedCSVUsers) {
                //Search borrower with borrowerNr
                Borrower borrower = allActiveUsers.stream()
                        .filter(activeBorrower -> Objects.equals(borrowerCSV.getBorrowerNr(), activeBorrower.getBorrowerNr()))
                        .findAny()
                        .orElse(null);

                if(borrower !=null){
                    //Borrower present ==> update borrower
                    updateBorrowerInformation(borrower,borrowerCSV);
                    allActiveUsers.remove(borrower);
                }
                else {
                    //Borrower not present ==> new borrower
                    //store new borrower as DTO
                    newBorrowerDTOs.add(createNewBorrowerDTO(borrowerCSV));
                }
            }
            //Remaining borrowers will be deactivated
            softDelete(allActiveUsers); //TODO: Use thread for better performance?
        }
        catch (IOException e) {
            throw new RuntimeException("fail to store csv data "+e.getMessage());
        }

        /*
            passwordEncoder ist resource heavy, so use a separate thread for storing the new borrower entities
         */
        Thread safeNewBorrowers = new Thread(() ->
                newBorrowerDTOs.forEach(dto -> {
                    Borrower newBorrower = modelMapper.map(dto, Borrower.class);
                    newBorrower.setPassword(passwordEncoder.encode(dto.getOneTimePassword()));
                    newBorrower.setBorrowerState(BorrowerState.INITIALIZED);
                    newBorrower.setDob(LocalDate.parse(dto.getDob()));
                    System.out.println(newBorrower);
                    borrowerRepository.save(newBorrower);
        }));
        safeNewBorrowers.start();
        return newBorrowerDTOs;
    }

    /**
     * Soft Delete a borrower. The borrower entity will still be in the database but marked as deleted or rather as "left the school".
     * @param borrowerNr The borrower number.
     */
    public void softDeleteBorrowerByNr(Long borrowerNr) {
        //TODO:No usage: individually deleting borrowers can lead to conflicts ?
        Optional<Borrower> toDeleteBorrower = borrowerRepository.findBorrowerByBorrowerNr(borrowerNr);
        if(toDeleteBorrower.isPresent()){
            Borrower borrower = toDeleteBorrower.get();
            borrower.setLeftTheSchool(true);
            borrower.setBorrowerNr(null);
            borrower.setEmail(null);
            borrower.setBorrowerState(BorrowerState.DEACTIVATED);
            borrower.setRoles(null);

            borrowerRepository.save(borrower);
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Borrower with borrowerNr:"+borrowerNr+" not found");
    }

    public InputStream downloadUsers() {
        return CSVHelper.usersToCSV(borrowerRepository.findAll());
    }

    /**
     * Creates a new Borrower Entity and stores it to  the Database.
     * @param newBorrower The new BorrowerEntity to be created
     * @return InitBorrowerDTO
     */
    private InitBorrowerDTO createNewBorrowerDTO(Borrower newBorrower) {
        String oneTimePassword = PasswordGenerator.generateInitialPassword();
        InitBorrowerDTO newBorrowerDTO = modelMapper.map(newBorrower, InitBorrowerDTO.class);
        System.out.println(newBorrowerDTO);
        newBorrowerDTO.setOneTimePassword(oneTimePassword);
        return newBorrowerDTO;
    }

    /**
     * Each Borrower in the list will be deactivated
     * @param inActiveBorrowers The User to be deactivated
     */
    private void softDelete(List<Borrower> inActiveBorrowers) {
        inActiveBorrowers.forEach(
                borrower -> {
                    borrower.setBorrowerNr(null);
                    borrower.setLeftTheSchool(true);
                    borrower.setEmail(null);
                    borrower.setBorrowerState(BorrowerState.DEACTIVATED);

                    borrowerRepository.save(borrower);
                });
    }

    private void updateBorrowerInformation(Borrower borrower, Borrower borrowerCSV) {
        borrower.setFirstName(borrowerCSV.getFirstName());
        borrower.setLastName(borrowerCSV.getLastName());
        borrower.setBorrowerGroup(borrowerCSV.getBorrowerGroup());
        borrower.setDob(borrowerCSV.getDob());
        borrower.setEmail(borrowerCSV.getEmail());

        borrowerRepository.save(borrower);
    }

    /**
     * Returns all Users which did not leave the school.
     * @return A list with active Users (Still on school).
     */
    private List<Borrower> getAllActiveUsers() {
        return borrowerRepository.findAllByBorrowerNrIsNotNull();
    }
}
