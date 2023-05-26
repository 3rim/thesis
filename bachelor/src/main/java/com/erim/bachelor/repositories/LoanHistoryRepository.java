package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.LoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanHistoryRepository extends JpaRepository<LoanHistory,Long> {

    Optional<LoanHistory> findLoanHistoryByMediumMediumID(Long mediumID);
}
