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
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Medium {

    @Id
    private Long mediumID;
    private String serialNr;
    private LocalDate dateOfLend;
    @Enumerated(EnumType.STRING)
    private Status status;

    /*private String mediumTyp;
    private String ISBN;
    @ElementCollection // sonst mekert er
    private Set<Integer> year = new HashSet<>();
    @ElementCollection
    private Set<String> subjects = new HashSet<>();
    private double originalPrice;
*/
    public Medium(Long mediumID) {
        this.mediumID = mediumID;
    }
    public Medium(Long mediumID,String serialNr) {
        this.mediumID = mediumID;
        this.serialNr = serialNr;
    }

    /*
    A many-to-one mapping means that many instances of this entity are mapped to one instance of another entityA many-to-one mapping means that many instances of this entity are mapped to one instance of another entity
     */
    @ManyToOne
    @JoinColumn(name = "borrower_id")
    @JsonIgnoreProperties(value = {"mediumList", "handler","hibernateLazyInitializer"}, allowSetters = true)
    private Borrower borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_series_id")
    private MediaSeries mediaSeries;

    /*
     CascadeType.REMOVE : if an Entity of Medium is removed, also remove the associated Entities in LendHistory.
     */
    @OneToMany(mappedBy = "medium",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoanHistory> loanHistories = new ArrayList<>();

    /**
     * Check if a Medium is borrowed or not.
     * @return true if Medium is borrowed by a user otherwise false
     */
    public boolean isBorrowed(){
        return this.getStatus() == Status.RENT;
    }

    public void addNewLoanHistory(LoanHistory loanHistory) {
        this.loanHistories.add(loanHistory);
        /*
            https://vladmihalcea.com/jpa-hibernate-synchronize-bidirectional-entity-associations/
            How to synchronize bidirectional entity associations with JPA and Hibernate
         */
        //loanHistory.setMedium(this);

    }
}
