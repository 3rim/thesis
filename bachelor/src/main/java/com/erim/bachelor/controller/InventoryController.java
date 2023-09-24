package com.erim.bachelor.controller;

import com.erim.bachelor.dto.*;
import com.erim.bachelor.entities.MediaSeries;
import com.erim.bachelor.entities.Medium;
import com.erim.bachelor.enums.Status;
import com.erim.bachelor.exceptions.MediaSeriesNotEmptyException;
import com.erim.bachelor.exceptions.MediumIdExistsException;
import com.erim.bachelor.exceptions.MediumIsBorrowedException;
import com.erim.bachelor.service.IInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/v1/inventory")
@Tag(name = "Inventory")
public class InventoryController {

    private final IInventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public InventoryController(IInventoryService inventoryService, ModelMapper modelMapper) {
        this.inventoryService = inventoryService;
        this.modelMapper = modelMapper;
    }

    /**
     * Returns All stored MediaSeries
     * @return list of Inventory
     */
    @Operation(summary = "Get inventory overview", description = "Returns list of MediaSeries")
    @GetMapping()
    public List<MediaSeries> getInventoryOverView(){return inventoryService.getInventoryOverview();}


    /**
     * Get medium by id
     * @param mediumID id of Medium
     * @return If Media exist return the Media if not return Http.NOT_FOUND
     */
    @Operation(summary = "Get a specific medium by id", description = "Returns medium by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The medium was not found",content = @Content)
    })
    @GetMapping(path = "{mediumID}")
    public ResponseEntity<MediumResponse> getMediumById(@PathVariable(value = "mediumID") Long mediumID){
        try {
            Medium medium = inventoryService.getMedium(mediumID);
            return new ResponseEntity<>(convertToDTO(medium),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medium not found");
        }
    }


    /**
     * Get medium by serialnumber.
     * @param serialNr serialnumber
     * @return List witch every medium that matched search via serialNr.
     */
    @GetMapping(path = "serialNr")
    public ResponseEntity<List<MediumResponse>> getMediumBySerialNr(@RequestParam String serialNr){
        try {
            List<Medium> mediaList = inventoryService.getMediumBySerialNr(serialNr);
            List<MediumResponse> response = mediaList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a specific MediaSeries by its id
     * @param seriesID the id of the MediaSeries
     * @return MediaSeries
     */
    @Operation(summary = "Get a specific mediaSeries by id", description = "Returns mediaSeries by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The mediaSeries was not found",content = @Content)
    })
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
    @Operation(summary = "Get Media in  MediaSeries", description = "Returns List of Media from a MediaSeries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The mediaSeries was not found",content = @Content)
    })
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
    @Operation(summary = "Create new MediaSeries", description = "Returns created MediaSeries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
    })
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
     * @param seriesID The MediaSeriesID
     * @return ResponseEntity
     */
    @Operation(summary = "Add new Medium to a MediaSeries", description = "Returns added Medium")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added"),
            @ApiResponse(responseCode = "400", description = "Bad Request - MediumID already exists",content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - The mediaSeries was not found",content = @Content)
    })
    @PostMapping(path = "series/{seriesID}/media" ,consumes = "application/json", produces = "application/json")
    public ResponseEntity<MediumResponse> addMedium(@RequestBody MediumRequest mediumRequest, @PathVariable Long seriesID) {
        Medium newMedium;
        Medium medium = null;
        try {
            medium = convertToMedium(mediumRequest);
            newMedium = inventoryService.addNewMedium(medium, seriesID);
            return new ResponseEntity<>(convertToDTO(newMedium), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mediaSeries: " +seriesID+" not found");
        } catch (MediumIdExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medium ID: " +medium.getMediumID()+ " already exists");
        }
    }

    /**
     * Patch a MediaSeries by its ID
     *
     * @param mediaSeries The MediaSeriesData in JSON
     * @param seriesID The id of the to patch MediaSeries
     * @return The patched MediaSeries
     */
    @Operation(summary = "Patch MediaSeries", description = "Returns patched MediaSeries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully patched"),
            @ApiResponse(responseCode = "404", description = "Not found - The mediaSeries was not found",content = @Content)
    })
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
    @Operation(summary = "Delete Medium by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "406", description = "Bad Request - Cannot delete borrowed medium",content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - The Medium was not found",content = @Content)
    })
    @DeleteMapping(path = "{mediumID}")
    public ResponseEntity<String>deleteById(@PathVariable(value = "mediumID")Long mediumID){
        try {
            inventoryService.deleteMedium(mediumID);
            return new ResponseEntity<>("Medium deleted", HttpStatus.OK);
        } catch (MediumIsBorrowedException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
        }catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Delete MediaSeries by id", description = "Deletion works only if MediaSeries does not contain Media")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad Request - MediaSeries contains Media",content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found - The mediaSeries was not found",content = @Content)
    })
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
