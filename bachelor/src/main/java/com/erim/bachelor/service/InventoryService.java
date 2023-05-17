package com.erim.bachelor.service;

import com.erim.bachelor.data.Status;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;


    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Medium> getAllMedia(){
        return inventoryRepository.findAll();
    }

    public Medium save(Medium medium){
        return inventoryRepository.save(medium);
    }

    public Optional<Medium> getMediumById(Long id) {
        return inventoryRepository.findById(id);
    }

    public  Optional<Medium> updateMedium(Long id,Medium newMedium) {
        return Optional.of(inventoryRepository.findById(id)
                .map(medium -> {
                    medium.setTitle(newMedium.getTitle());

                    return inventoryRepository.save(medium);
                })
                .orElseGet(() -> inventoryRepository.save(newMedium)));
    }

    public ResponseEntity<String> deleteMedium(Long id){
        if(inventoryRepository.findById(id).isPresent()){
            Medium medium = inventoryRepository.findById(id).get();
            if (!medium.isBorrowed()) {
                inventoryRepository.deleteById(id);
            }
            return new ResponseEntity<>("Medium with Id:"+id+" is still borrowed to User: "+medium.getBorrower(),HttpStatus.CONFLICT);
        }
        else
            return new ResponseEntity<>("Medium with Id:"+id+" not found",HttpStatus.NOT_FOUND);
    }
}
