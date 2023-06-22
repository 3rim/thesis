package com.erim.bachelor.repositories;

import com.erim.bachelor.data.InventoryDTO;
import com.erim.bachelor.entities.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Medium,Long> {

    /*
        SELECT Title,count(*) as amount,count(case when status='AVAILABLE' THEN 1 END) as available from medium group by 1;
     */
    @Query(nativeQuery = true)
    List<InventoryDTO> getInventoryDTO();

    List<Medium> findAllByTitle(String title);

}
