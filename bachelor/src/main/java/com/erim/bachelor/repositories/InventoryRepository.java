package com.erim.bachelor.repositories;

import com.erim.bachelor.entities.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Medium,Long> {
}
