package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.LoanHistory;
import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LoanHistoryRepositoryTest {

    @Autowired
    LoanHistoryRepository loanHistoryRepository;
    @Autowired
    BorrowerRepository borrowerRepository;
    @Autowired
    MediumRepository mediumRepository;

    @BeforeEach
    void setUp() {
        loanHistoryRepository.deleteAll();



        Borrower borrower = new Borrower(1L,"Erim","Medi");
        borrowerRepository.save(borrower);

        Medium medium = Medium.builder()
                .mediumID(1L)
                .borrower(borrower)
                .build();
        mediumRepository.save(medium);
        mediumRepository.flush();

        ArrayList<LoanHistory> loans = new ArrayList<>(
                Arrays.asList(
                        new LoanHistory(LocalDate.now().plus(3, ChronoUnit.DAYS),borrower,medium),
                        new LoanHistory(LocalDate.now().plus(16,ChronoUnit.DAYS),borrower,medium)

                ));
        loanHistoryRepository.saveAll(loans);

    }


    @Test
    void findAllLoanHistoryByMediumMediumID() {
        List<LoanHistory> medium1LoanHistory = loanHistoryRepository.findAllLoanHistoryByMediumMediumID(1L);
        Assertions.assertEquals(2,medium1LoanHistory.size());
    }
}