package com.erim.bachelor.entities;

import com.erim.bachelor.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Medium {

    @Id
    private Long mediumID;
    private String serialNr;
    private LocalDate dateOfLend;
    @Enumerated(EnumType.STRING)
    private Status status;

    //Many instances of this Medium are mapped to one instance Borrower
    @ManyToOne
    @JoinColumn(name = "borrower_id")
    @JsonIgnoreProperties("mediumList")
    private Borrower borrower;

    @ManyToOne
    @JoinColumn(name = "media_series_id")
    private MediaSeries mediaSeries;

    @OneToMany(mappedBy = "medium",cascade=CascadeType.ALL)
    private List<LoanHistory> loanHistories = new ArrayList<>();

    /**
     * Check if a Medium is borrowed or not.
     * @return true if Medium is borrowed by a user otherwise false
     */
    public boolean isBorrowed(){
        return this.getStatus() == Status.RENT;
    }


    /**
     *
     * Synchronize bidirectional entity associations with JPA and Hibernate
     * Due to Medium being the Entity to be persisted first ,weather for inventory or loaning, we need to sync LoanHistory and Medium.
     * We add new LoanHistories to a Medium and persist the Medium. The LoanHistories will be updated/persisted automatically by JPA.
     * ht<a href="tps://vladmihalcea.com/jpa-hibernate-synchronize-bidirectional-entity-associations/"></a>    */
    public void addNewLoanHistory(LoanHistory loanHistory) {
        this.loanHistories.add(loanHistory);
    }
}
