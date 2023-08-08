package com.erim.bachelor.controller;

import com.erim.bachelor.data.*;
import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.enums.Status;
import com.erim.bachelor.repositories.MediumRepository;
import com.erim.bachelor.repositories.MediaSeriesRepository;
import com.erim.bachelor.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;
    private final MediumRepository repository;
    private final MediaSeriesRepository mediaSeriesRepository;

    @Autowired
    public InventoryController(InventoryService inventoryService, ModelMapper modelMapper, MediumRepository repository, MediaSeriesRepository mediaSeriesRepository) {
        this.inventoryService = inventoryService;
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.mediaSeriesRepository = mediaSeriesRepository;
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
    public List<MediaSeries> getInventoryOverView(){
        return mediaSeriesRepository.findAll();
    }

    /**
     * Get whole inventory
     * @return list of all inventory
     */
    @GetMapping(path = "/allMedia")
    public List<MediumResponse> getAllMedia(){
        List<Medium> allMedia = inventoryService.getAllMedia();
        return allMedia.stream().map(this::convertToDTO).toList();
    }
    /**
     * Get every Medium in a MediaSeries
     * @return list of all Media in a MediaSeries
     */
    @GetMapping(path = "title/{title}")
    public ResponseEntity<List<MediumResponse>> getAllMediaByTitle(@PathVariable(value = "title") String title){

        List<Medium> allMediaByTitle = null;
        try {
            allMediaByTitle = inventoryService.getAllMediaByTitle(title);
            List<MediumResponse> response = allMediaByTitle.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * Get medium by id
     * @param id id of Medium
     * @return If Media exist return the Media and Http.Ok if not return Http.NOT_FOUND
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<MediumResponse> getMediumById(@PathVariable(value = "id") Long id){
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
    public ResponseEntity<MediumResponse> addMedium(@RequestBody MediumRequestDTO mediumRequestDTO){
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
     * Converts a Medium into MediumResponse
     * @param medium The medium to be converted
     * @return The converted MediumResponse
     */
    private MediumResponse convertToDTO(Medium medium){
        MediumResponse dto = modelMapper.map(medium, MediumResponse.class);
        dto.setTitle(medium.getMediaSeries().getTitel());
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
