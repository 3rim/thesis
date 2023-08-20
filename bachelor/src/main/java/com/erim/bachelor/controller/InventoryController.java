package com.erim.bachelor.controller;

import com.erim.bachelor.dto.*;
import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.enums.Status;
import com.erim.bachelor.exceptions.MediaSeriesNotEmptyException;
import com.erim.bachelor.exceptions.MediumStillBorrowedException;
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

    @Autowired
    public InventoryController(InventoryService inventoryService, ModelMapper modelMapper) {
        this.inventoryService = inventoryService;
        this.modelMapper = modelMapper;
    }

    /**
     * Returns All stored MediaSeries
     * @return list of Inventory
     */
    @GetMapping()
    public List<MediaSeries> getInventoryOverView(){return inventoryService.getInventoryOverview();}


    /**
     * Get medium by id
     * @param mediumID id of Medium
     * @return If Media exist return the Media if not return Http.NOT_FOUND
     */
    @GetMapping(path = "{mediumID}")
    public ResponseEntity<MediumResponse> getMediumById(@PathVariable(value = "mediumID") Long mediumID){
        try {
            Medium medium = inventoryService.getMediumById(mediumID);
            return new ResponseEntity<>(convertToDTO(medium),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medium not found");
        }
    }

    /**
     * Get a specific MediaSeries by its id
     * @param seriesID the id of the MediaSeries
     * @return MediaSeries
     */
    @GetMapping(path = "series/{seriesID}")
    public ResponseEntity<MediaSeries> getMediaSeries(@PathVariable Long seriesID){
        try {
            return new ResponseEntity<>(inventoryService.getMediaSeries(seriesID),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all Media from a MediaSeries
     * @param seriesID the id of the MediaSeries
     * @return List which contains every Media of a MediaSeries
     */
    @GetMapping(path = "series/{seriesID}/media")
    public ResponseEntity<List<MediumResponse>> getMediaSeriesMedia(@PathVariable Long seriesID){
        try {
            List<Medium> mediaList = inventoryService.getMediaSeriesMedia(seriesID);
            List<MediumResponse> response = mediaList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add a new MediaSeries
     * @param series FormData of MediaSeries
     * @return The new added MediaSeries
     */
    @PostMapping(path = "/series")
    public ResponseEntity<MediaSeries> createNewMediaSeries(@RequestBody MediaSeries series){
        try {
            return new ResponseEntity<>(inventoryService.createNewMediaSeries(series),HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add new Medium to a MediaSeries.
     * @param mediumRequest The Medium to be added into the MediaSeries
     * @param seriesId The MediaSeriesID
     * @return ResponseEntity
     */
    @PostMapping(path = "series/{seriesId}/media" ,consumes = "application/json", produces = "application/json")
    public ResponseEntity<MediumResponse> addMedium(@RequestBody MediumRequest mediumRequest, @PathVariable Long seriesId){
        Medium newMedium;
        try {
            Medium medium = convertToMedium(mediumRequest);
            newMedium = inventoryService.addNewMedium(medium,seriesId);
            if(newMedium == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medium ID: " +medium.getMediumID() +" already exists");
            }else {
                return new ResponseEntity<>(convertToDTO(newMedium), HttpStatus.OK);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "{id}",consumes = "application/json", produces = "application/json")
    public ResponseEntity<Medium> updateMedium(@PathVariable(value = "id")Long id, @RequestBody Medium updateMedium){
        //TODO:refactor
        Optional<Medium> medium = inventoryService.updateMedium(id,updateMedium);
        if(medium.isPresent())
            return new ResponseEntity<>(medium.get(),HttpStatus.OK);
        else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Medium not found"
            );
    }

    /**
     * Patch a MediaSeries by its ID
     *
     * @param mediaSeries The MediaSeriesData in JSON
     * @param seriesID The id of the to patch MediaSeries
     * @return The patched MediaSeries
     */
    @PatchMapping(path = "series/{seriesID}")
    public ResponseEntity<MediaSeries> patchMediaSeries(@RequestBody MediaSeries mediaSeries, @PathVariable Long seriesID){
        try {
            MediaSeries patchedMediaSeries = inventoryService.patchMediaSeries(seriesID,mediaSeries);
            return new ResponseEntity<>(patchedMediaSeries,HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete a medium by id
     * @param mediumID id of medium
     * @return StatusResponse
     */
    @DeleteMapping(path = "{mediumID}")
    public ResponseEntity<String>deleteById(@PathVariable(value = "mediumID")Long mediumID){
        try {
            inventoryService.deleteMedium(mediumID);
            return new ResponseEntity<>("Medium deleted", HttpStatus.OK);
        } catch (MediumStillBorrowedException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(path = "series/{seriesID}")
    public ResponseEntity<String> deleteMediaSeries(@PathVariable Long seriesID){
        try {
            inventoryService.deleteMediaSeries(seriesID);
            return new ResponseEntity<>("MediaSeries deleted",HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("MediaSeries:"+seriesID+" not found",HttpStatus.NOT_FOUND);
        }catch (MediaSeriesNotEmptyException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Converts a Medium into MediumResponse
     * @param medium The medium to be converted
     * @return The converted MediumResponse
     */
    private MediumResponse convertToDTO(Medium medium){
        MediumResponse dto = modelMapper.map(medium, MediumResponse.class);
        MediaSeries mediaSeries = medium.getMediaSeries();
        dto.setTitle(mediaSeries.getTitle());
        dto.setMediaSeriesID(mediaSeries.getId());
        if(medium.isBorrowed())
            dto.setCurrentBorrower(medium.getBorrower().getFullName());
        return dto;
    }

    /**
     * Converts a MediumRequestDTO into a Medium.
     * @param mediumRequest The MediumRequestDTO to be converted into a Medium
     * @return The converted Medium
     */
    private Medium convertToMedium(MediumRequest mediumRequest){
        Medium medium = modelMapper.map(mediumRequest, Medium.class);
        medium.setStatus(Status.AVAILABLE);
        return medium;
    }

}
