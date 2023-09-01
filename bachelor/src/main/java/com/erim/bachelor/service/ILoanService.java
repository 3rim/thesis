package com.erim.bachelor.service;

import com.erim.bachelor.entities.Borrower;
import com.erim.bachelor.entities.LoanHistory;
import com.erim.bachelor.exceptions.MediumIsBorrowedException;

import java.util.List;

public interface ILoanService {

    /**
     * loan or unloan medium to borrower. If the given mediumID is already loaned from the given borrower unloan the medium from the borrower
     * otherwise loan it to him.
     * Furthermore, store loan/unloan date for the Medium
     * @param borrowerID The BorrowerID
     * @param mediumID The MediumID
     * @return The Borrower to which Medium was loaned or un-loaned.
     */
    Borrower loanUnloanMediumToUser(Long borrowerID,Long mediumID) throws MediumIsBorrowedException;

    /**
     * Get LoanHistories for the Medium
     * @param mediumID The ID of Medium
     * @return List containing each LoanHistory
     */
    List<LoanHistory> getLoanHistories(Long mediumID);


}
