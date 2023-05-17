package com.erim.bachelor.controller;

import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    /**
     * Get whole inventory
     * @return list of all inventory
     */
    @GetMapping(path = "/all")
    public List<Medium> getAllMedia(){
        return inventoryService.getAllMedia();
    }

    /**
     * Get medium by id
     * @param id id of Medium
     * @return If Media exist return the Media and Http.Ok if not return Http.NOT_FOUND
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<Medium> getMediumById(@PathVariable(value = "id") Long id){
        Optional<Medium> medium = inventoryService.getMediumById(id);
        if(medium.isPresent())
            return new ResponseEntity<>(medium.get(),HttpStatus.OK);
        else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Medium not found"
            );
    }

    /**
     * Add new Medium to the inventory.
     * @param medium The Medium to be added into the inventory
     * @return ResponseEntity
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Medium> addMedium(@RequestBody Medium medium){
        Medium newMedium = inventoryService.save(medium);
        return new ResponseEntity<>(newMedium, HttpStatus.OK);
    }

    /**
     * Update a medium by id
     * @param id id of medium
     * @param updateMedium The new medium
     * @return
     */
    @PutMapping(path = "{id}",consumes = "application/json", produces = "application/json")
    public ResponseEntity<Medium> updateMedium(@PathVariable(value = "id")Long id, @RequestBody Medium updateMedium){

        Optional<Medium> medium = inventoryService.updateMedium(id,updateMedium);
        if(medium.isPresent())
            return new ResponseEntity<>(medium.get(),HttpStatus.OK);
        else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Medium not found"
            );

    }

    /**
     * Delete a medium by id
     * @param id id of medium
     * @return
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<String>deleteById(@PathVariable(value = "id")Long id){
        return inventoryService.deleteMedium(id);
    }

}
