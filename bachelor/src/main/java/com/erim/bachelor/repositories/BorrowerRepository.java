package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower,Long> {

    // LIKE '%or%'	Finds any values that have "or" in any position
    //:value  parameter binding
    @Query("select u from Borrower u where (:firstName is null or u.firstName like %:firstName%)"
            +" and (:lastName is null or u.lastName  like %:lastName%)")
    List<Borrower> searchByFirstAndOrLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    Optional<Borrower> findBorrowerByBorrowerNr(Long borrowerNr);


}
