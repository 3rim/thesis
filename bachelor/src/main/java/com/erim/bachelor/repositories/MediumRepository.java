package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediumRepository extends JpaRepository<Medium,Long> {
    /**
     * Search by SerialNumber of a medium. Returns every match.
     *
     * @param serialNr serialNr
     * @return List containing every Media which matches witch serialNr
     */
    List<Medium> findBySerialNrContainsIgnoreCase(String serialNr);
}
