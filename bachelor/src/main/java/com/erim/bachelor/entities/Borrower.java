package com.erim.bachelor.entities;

import com.erim.bachelor.enums.BorrowerState;
import com.erim.bachelor.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Borrower implements UserDetails {
    @Id
    @GeneratedValue
    private Long borrowerID;
    @Column(unique = true)
    private Long borrowerNr; //Fachlicher Schlüssel
    private String firstName;
    private String lastName;
    private String email;

    //date of birth
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private BorrowerState borrowerState;
    private String password;
    private boolean leftTheSchool;

    @Column(name = "class")
    private String borrowerGroup;

    /*
    OneToMany: One Borrower can have Many Mediums
    The mappedBy property is what we use to tell Hibernate which variable we are using to represent the parent class in our child class
     */
    @OneToMany(mappedBy = "borrower")
    @JsonIgnoreProperties("borrower")
    private List<Medium> mediumList = new ArrayList<>();

    public Borrower(String firstName,String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Borrower(long borrowerNr, String firstName, String lastName) {
        this(borrowerNr,firstName,lastName,"none",LocalDate.now(),"dummy@mail");
    }

    public Borrower(long borrowerNr, String firstName, String lastName,String borrowerGroup) {
        this(borrowerNr,firstName,lastName,borrowerGroup,LocalDate.now(),"dummy@mail");
    }
    public Borrower(long borrowerNr, String firstName, String lastName,String borrowerGroup,LocalDate dob,String email) {
        this.borrowerNr = borrowerNr;
        this.firstName = firstName;
        this.lastName = lastName;
        this.borrowerGroup = borrowerGroup;
        this.dob = dob;
        this.email = email;
    }


    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
