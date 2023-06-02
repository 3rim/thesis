package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.LoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanHistoryRepository extends JpaRepository<LoanHistory,Long> {

    List<LoanHistory> findAllLoanHistoryByMediumMediumID(Long mediumID);
}
