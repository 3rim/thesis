package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.LoanHistory;
import com.erim.bachelor.entities.Medium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
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

        Medium medium = new Medium(1L,"Spring Boot 2");
        mediumRepository.save(medium);
        Medium medium2 = new Medium(2L,"Java ist auch eine Insel");
        mediumRepository.save(medium);


        ArrayList<LoanHistory> loans = new ArrayList<>(
                Arrays.asList(
                        new LoanHistory(LocalDate.now(),borrower,medium),
                        new LoanHistory(LocalDate.now().plus(3, ChronoUnit.DAYS),borrower,medium),
                        new LoanHistory(LocalDate.now().plus(16,ChronoUnit.DAYS),borrower,medium),

                        new LoanHistory(LocalDate.now(),borrower,medium2)

                ));
        loanHistoryRepository.saveAll(loans);

    }

    @Test
    void findLoanHistoryByMediumMediumID() {
        List<LoanHistory> medium1LoanHistory = loanHistoryRepository.findAllLoanHistoryByMediumMediumID(1L);
        assertThat(medium1LoanHistory.size(),is(3));

        List<LoanHistory> medium2LoanHistory = loanHistoryRepository.findAllLoanHistoryByMediumMediumID(2L);
        assertThat(medium2LoanHistory.size(),is(1));
    }
}