package com.erim.bachelor.controller;

import com.erim.bachelor.data.*;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.enums.Status;
import com.erim.bachelor.repositories.InventoryRepository;
import com.erim.bachelor.service.InventoryService;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final InventoryRepository repository;

    @Autowired
    public InventoryController(InventoryService inventoryService, ModelMapper modelMapper, InventoryRepository repository) {
        this.inventoryService = inventoryService;
        this.modelMapper = modelMapper;
        this.repository = repository;
    }

    /**
     * Get list of inventoryDTO´s
     * Title| Amount | Available
     * IPad     4          2
     * Book1    15         0
     *
     * @return list of InventoryDTO´s
     */
    @GetMapping()
    public List<InventoryDTO> getInventory(){
        return inventoryService.getInventoryDTO();
    }

    /**
     * Get whole inventory
     * @return list of all inventory
     */
    @GetMapping(path = "/allMedia")
    public List<MediumResponseDTO> getAllMedia(){
        List<Medium> allMedia = inventoryService.getAllMedia();
        return allMedia.stream().map(this::convertToDTO).toList();
    }
    /**
     * Get whole inventory
     * @return list of all inventory
     */
    @GetMapping(path = "title/{title}")
    public List<MediumResponseDTO> getAllMediaByTitle(@PathVariable(value = "title") String title){
        List<Medium> allMediaByTitle = inventoryService.getAllMediaByTitle(title);
        return allMediaByTitle.stream().map(this::convertToDTO).toList();
    }



    /**
     * Get medium by id
     * @param id id of Medium
     * @return If Media exist return the Media and Http.Ok if not return Http.NOT_FOUND
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<MediumResponseDTO> getMediumById(@PathVariable(value = "id") Long id){
        Optional<Medium> medium = inventoryService.getMediumById(id);
        if(medium.isPresent())
            return new ResponseEntity<>(convertToDTO(medium.get()),HttpStatus.OK);
        else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Medium not found"
            );
    }

    /**
     * Add new Medium to the inventory.
     * @param mediumRequestDTO The Medium to be added into the inventory
     * @return ResponseEntity
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<MediumResponseDTO> addMedium(@RequestBody MediumRequestDTO mediumRequestDTO){
        Medium medium = convertToMedium(mediumRequestDTO);
        Medium mediumCreated = inventoryService.addNewMedium(medium);

        if(mediumCreated == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medium ID: " +medium.getMediumID() +" already exists");
        }else {
            return new ResponseEntity<>(convertToDTO(mediumCreated), HttpStatus.OK);
        }
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

    /**
     * Converts a Medium into MediumResponseDTO
     * @param medium The medium to be converted
     * @return The converted MediumResponseDTO
     */
    private MediumResponseDTO convertToDTO(Medium medium){
        MediumResponseDTO dto = modelMapper.map(medium, MediumResponseDTO.class);
        if(medium.isBorrowed())
            dto.setCurrentBorrower(medium.getBorrower().getFullName());
        return dto;
    }

    /**
     * Converts a MediumRequestDTO into a Medium.
     * @param mediumRequestDTO The MediumRequestDTO to be converted into a Medium
     * @return The converted Medium
     */
    private Medium convertToMedium(MediumRequestDTO mediumRequestDTO){
        Medium medium = modelMapper.map(mediumRequestDTO, Medium.class);
        medium.setStatus(Status.AVAILABLE);
        return medium;
    }

}
