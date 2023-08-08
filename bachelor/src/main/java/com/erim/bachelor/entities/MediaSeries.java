package com.erim.bachelor.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class MediaSeries {
    @Id
    @GeneratedValue
    private Long id;
    private String ISBN_EAN;
    private String titel;
    private String mediaTyp;
    @ElementCollection // Jahrgänge
    private Set<Integer> vintage = new HashSet<>();
    @ElementCollection //Fächer
    private Set<String> subjects = new HashSet<>();

    @Formula(value = "(SELECT COUNT(*) FROM medium m WHERE m.media_series_id=id)")
    private int amount;
    @Formula(value = "(SELECT COUNT(*) FROM medium m WHERE m.media_series_id=id AND m.status='AVAILABLE')")
    private int available;
    private double originalPrice;
    @OneToMany(mappedBy = "mediaSeries")
    @JsonIgnore
    private List<Medium> mediumList;
}
