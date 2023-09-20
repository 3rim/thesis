package com.erim.bachelor.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) //https://www.baeldung.com/spring-swagger-hide-field
    private Long id;
    private String ISBN_EAN;
    @NotNull
    private String title;
    private String mediaTyp;
    @ElementCollection // Jahrgänge
    private Set<Integer> vintage = new HashSet<>();
    @ElementCollection //Fächer
    private Set<String> subjects = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Formula(value = "(SELECT COUNT(*) FROM medium m WHERE m.media_series_id=id)")
    private int amount;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Formula(value = "(SELECT COUNT(*) FROM medium m WHERE m.media_series_id=id AND m.status='AVAILABLE')")
    private int available;
    private double originalPrice;
    @OneToMany(mappedBy = "mediaSeries", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Medium> mediumList;
}
