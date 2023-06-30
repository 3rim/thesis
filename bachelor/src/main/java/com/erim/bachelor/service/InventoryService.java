package com.erim.bachelor.service;

import com.erim.bachelor.data.InventoryDTO;
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

    /**
     * Add new Medium to inventory if id does not exist already otherwise return null
     *
     * @param medium The new Medium to be added
     * @return null or the added Medium
     */
    public Medium addNewMedium(Medium medium){
        if(!inventoryRepository.existsById(medium.getMediumID()))
            return inventoryRepository.save(medium);
        else {
            return null;
        }
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

        Optional<Medium> mediumToDelete = inventoryRepository.findById(id);
        if(mediumToDelete.isEmpty())
            return new ResponseEntity<>("Medium with id:"+id+" not found",HttpStatus.BAD_REQUEST);

        Medium medium = mediumToDelete.get();
        if(medium.isBorrowed())
            return new ResponseEntity<>("Medium with Id:"+id+" is still borrowed to User: "+medium.getBorrower().getFullName(),HttpStatus.CONFLICT);
        else {
            inventoryRepository.deleteById(id);
            return new ResponseEntity<>("Medium with Id:"+id+" deleted ",HttpStatus.OK);
        }




    }

    public List<Medium> getAllMediaByTitle(String title) {
        return inventoryRepository.findAllByTitle(title);
    }

    public List<InventoryDTO> getInventoryDTO() {
        return inventoryRepository.getInventoryDTO();
    }
}
