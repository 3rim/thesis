package com.erim.bachelor.entities;

import com.erim.bachelor.data.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table
public class Medium {

    @Id
    private Long mediumID;
    private String mediumTyp;
    private Status status;
    private String ISBN;
    private String serialNr;
    @ElementCollection // sonst mekert er
    private Set<Integer> year = new HashSet<>();
    @ElementCollection
    private Set<Integer> subjects = new HashSet<>();
    private double originalPrice;
    private String title;

    /*
    A many-to-one mapping means that many instances of this entity are mapped to one instance of another entityA many-to-one mapping means that many instances of this entity are mapped to one instance of another entity
     */
    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    /*
     CascadeType.REMOVE : if an Entity of Medium is removed, also remove the associated Entities in LendHistory.
     */
    @OneToMany(mappedBy = "lendHistoryId",cascade = CascadeType.REMOVE)
    private List<LendHistory> lendHistories = new ArrayList<>();

}
