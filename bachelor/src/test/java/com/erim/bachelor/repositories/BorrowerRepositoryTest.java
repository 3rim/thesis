package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.Borrower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
@DataJpaTest
class BorrowerRepositoryTest {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @BeforeEach
    void setUp() {
        borrowerRepository.deleteAll();
        ArrayList<Borrower> borrowers = new ArrayList<>(
                Arrays.asList(
                        new Borrower(4,"Marck","Schmidt"),
                        new Borrower(18,"Max Maximus","Mustermann"),
                        new Borrower(3,"Max","Maxi"),
                        new Borrower(27,"Lisa","Hansen")
                ));
        borrowerRepository.saveAll(borrowers);
    }

    @Test
    void searchByFirstAndOrLastName_findAllWithFirstName_Max() {
        List<Borrower> borrowers = borrowerRepository.searchByFirstAndOrLastName("Max","");
        assertThat(borrowers,hasSize(2));

        List<String> names = borrowers.stream().map(Borrower::getFirstName).toList();
        assertThat(names,containsInAnyOrder("Max","Max Maximus"));
    }
    @Test
    void searchByFirstAndOrLastName_findAllByLastName_Hansen() {
        List<Borrower> borrowers = borrowerRepository.searchByFirstAndOrLastName("","Hansen");
        assertThat(borrowers,hasSize(1));

        List<String> names = borrowers.stream().map(Borrower::getFirstName).toList();
        assertThat(names,containsInAnyOrder("Lisa"));
    }
    @Test
    void searchByFirstAndOrLastName_EmptyList() {
        List<Borrower> borrowers = borrowerRepository.searchByFirstAndOrLastName("Does not exist","");
        assertThat(borrowers,hasSize(0));
    }

    @Test
    void findBorrowerByBorrowerNr() {
        Borrower borrower = borrowerRepository.findBorrowerByBorrowerNr(27L).orElseThrow();
        assertThat(borrower.getBorrowerNr(),is(27L));
    }
}