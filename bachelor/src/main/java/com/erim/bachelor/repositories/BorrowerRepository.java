package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.enums.BorrowerState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower,Long> {

    // LIKE '%or%'	Finds any values that have "or" in any position
    //:value  parameter binding
    @Query("select u from Borrower u where (:firstName is null or u.firstName ilike %:firstName%)"
            +" and (:lastName is null or u.lastName  ilike %:lastName%)")
    List<Borrower> searchByFirstAndOrLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    Optional<Borrower> findBorrowerByBorrowerNr(Long borrowerNr);

    /**
     * return all Borrower Entities where BorrowerNr is not null
     * @return List of Borrowers where borrowerNr != null
     */
    List<Borrower> findAllByBorrowerNrIsNotNull();

    Page<Borrower> findAllByBorrowerState(Pageable pageable,BorrowerState borrowerState );

    /**
     * UPPER(COL_NAME)
     * @param pageable
     * @param firstName
     * @param lastName
     * @return
     */
    @Query("select u from Borrower u where (:firstName is null or  u.firstName ilike %:firstName%)"
            +" and (:lastName is null or u.lastName  ilike %:lastName%)")
    Page<Borrower> findAllByName(Pageable pageable, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("select u from Borrower u where (:firstName is null or u.firstName ilike %:firstName%)"
            +" and (:lastName is null or u.lastName  ilike %:lastName%) and u.borrowerState=:borrowerState")
    Page<Borrower> findAllByStateAndName(Pageable pageable, BorrowerState borrowerState, @Param("firstName") String firstName, @Param("lastName") String lastName);

    Optional<Borrower> findBorrowerByEmail(String email);


}
