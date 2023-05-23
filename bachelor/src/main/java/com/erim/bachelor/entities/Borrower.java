package com.erim.bachelor.entities;

import com.erim.bachelor.data.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Borrower {
    @Id
    @GeneratedValue
    private Long borrowerID;
    private String firstName;
    private String lastName;
    //date of birth
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles = new HashSet<>();
    private String password;
    /*
    OneToMany: One Borrower can have Many Mediums
    The mappedBy property is what we use to tell Hibernate which variable we are using to represent the parent class in our child class
     */
    @OneToMany(mappedBy = "borrower")
    private List<Medium> mediumList = new ArrayList<>();

    public Borrower(String firstName,String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public Borrower(long id, String firstName, String lastName) {
        this.borrowerID = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
