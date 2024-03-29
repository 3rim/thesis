package com.erim.bachelor.service;

import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.dto.InitBorrowerDTO;
import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.enums.Role;
import com.erim.bachelor.helper.CSVHelper;
import com.erim.bachelor.helper.PasswordGenerator;
import com.erim.bachelor.repositories.BorrowerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

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

    public Page<Borrower> getUsers(Pageable pageable) {
        return borrowerRepository.findAll(pageable);
    }

    public Page<Borrower> getByState(Pageable pageable,BorrowerState state){
        return borrowerRepository.findAllByBorrowerState(pageable,state);
    }

    public Page<Borrower> getAllByName(Pageable pageable,String firstName,String lastName){
        return borrowerRepository.findAllByName(pageable,firstName,lastName);
    }

    public Page<Borrower> getAllByStateAndName(Pageable pageable,BorrowerState state,String firstName,String lastName){
        return borrowerRepository.findAllByStateAndName(pageable,state,firstName,lastName);
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
            softDelete(allActiveUsers);
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
     * Permanent delete borrowers if nothing is borrowed by them and BorrowerState is not ACTIVE.
     * @param borrowerIDs List of Ids to be deleted
     */
    public void deleteBorrowersByID(List<Long> borrowerIDs){
        for(Long id : borrowerIDs){
            Optional<Borrower> borrower = borrowerRepository.findById(id);
            if(borrower.isPresent()){
                Borrower b = borrower.get();
                if(b.getMediumList().isEmpty() && b.getBorrowerState() != BorrowerState.ACTIVE){
                    b.getLoanHistories().forEach(loanHistory -> loanHistory.setBorrower(null));
                    borrowerRepository.deleteById(id);
                }
            }
        }
    }

    public InputStream downloadUsers() {
        return CSVHelper.usersToCSV(borrowerRepository.findAll());
    }

    public List<InitBorrowerDTO> resetPasswords(List<Long> borrowerIDs) {

        Map<Long,InitBorrowerDTO> resetBorrowerMap  = new HashMap<>();
        List<InitBorrowerDTO> dtoList = new ArrayList<>();

        borrowerIDs.forEach(id ->{
           Optional<Borrower> borrower = borrowerRepository.findById(id);
           if(borrower.isPresent()){
               InitBorrowerDTO dto = createNewBorrowerDTO(borrower.get());
               resetBorrowerMap.put(id,dto);
               dtoList.add(dto);
           }
        });

        Thread resetBorrowers = new Thread(() ->
                resetBorrowerMap.forEach((id,dto )-> {
                    Borrower resetBorrower = borrowerRepository.findById(id).get();
                    resetBorrower.setPassword(passwordEncoder.encode(dto.getOneTimePassword()));
                    resetBorrower.setBorrowerState(BorrowerState.INITIALIZED);
                    borrowerRepository.save(resetBorrower);
                }));
        resetBorrowers.start();

        return dtoList;
    }

    public Borrower updateBorrowerRoles(Long id, Set<Role> roles) {
        Borrower borrower = borrowerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        borrower.setRoles(roles);
        borrowerRepository.save(borrower);
        return borrower;
    }


    /**
     * Creates a new Borrower Entity and stores it to  the Database.
     * @param newBorrower The new BorrowerEntity to be created
     * @return InitBorrowerDTO
     */
    private InitBorrowerDTO createNewBorrowerDTO(Borrower newBorrower) {
        String oneTimePassword = PasswordGenerator.generateInitialPassword();
        InitBorrowerDTO newBorrowerDTO = modelMapper.map(newBorrower, InitBorrowerDTO.class);
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
