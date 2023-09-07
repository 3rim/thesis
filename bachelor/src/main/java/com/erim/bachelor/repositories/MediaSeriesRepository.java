package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaSeriesRepository extends JpaRepository<MediaSeries,Long> {

}
