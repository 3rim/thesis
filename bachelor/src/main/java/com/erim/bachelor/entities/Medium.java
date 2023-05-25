package com.erim.bachelor.entities;

import com.erim.bachelor.data.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Medium {

    @Id
    @GeneratedValue
    private Long mediumID;
    private String mediumTyp;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String ISBN;
    private String serialNr;
    @ElementCollection // sonst mekert er
    private Set<Integer> year = new HashSet<>();
    @ElementCollection
    private Set<Integer> subjects = new HashSet<>();
    private double originalPrice;
    private String title;

    public Medium(Long mediumID,String title) {
        this.mediumID = mediumID;
        this.title = title;

    }

    /*
    A many-to-one mapping means that many instances of this entity are mapped to one instance of another entityA many-to-one mapping means that many instances of this entity are mapped to one instance of another entity
     */
    @ManyToOne
    @JoinColumn(name = "borrower_id")
    @JsonIgnoreProperties(value = {"mediumList", "handler","hibernateLazyInitializer"}, allowSetters = true)
    private Borrower borrower;

    /*
     CascadeType.REMOVE : if an Entity of Medium is removed, also remove the associated Entities in LendHistory.
     */
    @OneToMany(mappedBy = "lendHistoryId",cascade = CascadeType.REMOVE)
    private List<LoanHistory> lendHistories = new ArrayList<>();

    /**
     * Check if a Medium is borrowed or not.
     * @return true if Medium is borrowed by a user otherwise false
     */
    public boolean isBorrowed(){
        return this.getStatus() == Status.RENT;
    }

}
