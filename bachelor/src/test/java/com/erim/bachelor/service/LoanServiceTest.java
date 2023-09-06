package com.erim.bachelor.service;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.enums.Status;
import com.erim.bachelor.exceptions.MediumIsBorrowedException;
import com.erim.bachelor.repositories.BorrowerRepository;
import com.erim.bachelor.repositories.MediumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private MediumRepository mediumRepository;
    @InjectMocks
    private LoanService loanService;

    private Borrower borrower;

    private Medium medium;
    private Medium borrowedMedium;

    @BeforeEach
    void setUp() {
       borrower = Borrower.builder()
               .borrowerID(1L)
               .borrowerNr(1L)
               .firstName("Erim")
               .lastName("Medi")
               .mediumList(new ArrayList<>())
               .build();

        Borrower borrowerWithMedium = Borrower.builder()
                .borrowerID(2L)
                .borrowerNr(2L)
                .firstName("Ensar")
                .lastName("Medi")
                .mediumList(new ArrayList<>())
                .build();

        medium = Medium.builder().mediumID(1L).loanHistories(new ArrayList<>()).build();
        borrowedMedium = Medium.builder().mediumID(2L).loanHistories(new ArrayList<>()).status(Status.RENT).build();

        borrowerWithMedium.getMediumList().add(borrowedMedium);
        borrowedMedium.setBorrower(borrowerWithMedium);
        borrowedMedium.getBorrower().setBorrowerID(borrowerWithMedium.getBorrowerID());
    }


    @Test
    void loanUnloanMedium_BorrowerNotFoundException() {
        Long notExistingID = 5L;
        when(borrowerRepository.findById(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () ->loanService.loanUnloanMedium(notExistingID,medium.getMediumID()));

        verify(mediumRepository,times(0)).save(any());
    }

    @Test
    void loanUnloanMedium_MediumNotFoundException() {
        Long notExistingID = 5L;
        //when(mediumRepository.findById(notExistingID)).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () ->loanService.loanUnloanMedium(borrower.getBorrowerID(),notExistingID));

        verify(mediumRepository,times(0)).save(any());
    }

    @Test
    void loanMedium_MediumIsBorrowedException(){
        when(borrowerRepository.findById(any())).thenReturn(Optional.ofNullable(borrower));
        when(mediumRepository.findById(any())).thenReturn(Optional.ofNullable(borrowedMedium));
        assertThrows(MediumIsBorrowedException.class,()->loanService.loanUnloanMedium(borrower.getBorrowerID(),borrowedMedium.getMediumID()));
        verify(mediumRepository,times(0)).save(any());
    }

    @Test
    void loanMedium() throws MediumIsBorrowedException {
        when(borrowerRepository.findById(any())).thenReturn(Optional.ofNullable(borrower));
        when(mediumRepository.findById(any())).thenReturn(Optional.ofNullable(medium));
        loanService.loanUnloanMedium(borrower.getBorrowerID(),medium.getMediumID());

        verify(mediumRepository,times(1)).save(medium);
    }
}