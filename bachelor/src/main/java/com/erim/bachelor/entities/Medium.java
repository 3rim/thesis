package com.erim.bachelor.entities;

import com.erim.bachelor.data.InventoryDTO;
import com.erim.bachelor.data.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(
        name = "Medium.getInventoryDTO",
        query = "SELECT Title,count(*) as amount,count (CASE WHEN status='AVAILABLE' THEN 1 END) as available from medium group by 1",
        resultSetMapping = "Mapping.InventoryDTO" )
@SqlResultSetMapping(name = "Mapping.InventoryDTO",classes = {
        @ConstructorResult(
                targetClass = InventoryDTO.class,
                columns = {
                        @ColumnResult(name = "Title"),
                        @ColumnResult(name = "amount",type = Integer.class),
                        @ColumnResult(name = "available",type = Integer.class)})
})
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
    private Set<String> subjects = new HashSet<>();
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
